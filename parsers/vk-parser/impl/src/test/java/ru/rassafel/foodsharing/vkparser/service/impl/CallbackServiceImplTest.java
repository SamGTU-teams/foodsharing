package ru.rassafel.foodsharing.vkparser.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.rassafel.foodsharing.common.model.mapper.RegionMapper;
import ru.rassafel.foodsharing.parser.model.dto.PostContext;
import ru.rassafel.foodsharing.parser.model.dto.RawPostDto;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;
import ru.rassafel.foodsharing.vkparser.model.mapper.RawPostMapper;
import ru.rassafel.foodsharing.vkparser.model.vk.Wallpost;
import ru.rassafel.foodsharing.vkparser.repository.GroupRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author rassafel
 */
class CallbackServiceImplTest {
    @InjectMocks
    CallbackServiceImpl service;

    @Mock
    GroupRepository repository;

    @Mock
    RabbitTemplate template;

    @Spy
    RawPostMapper rawPostMapper = RawPostMapper.INSTANCE;

    @Spy
    RegionMapper regionMapper = RegionMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @MethodSource("ru.rassafel.foodsharing.vkparser.service.impl.SecretMatches#registerWithoutAccessMatchValues")
    void wallpostNewGroupExist(String dbSecret, String acceptedSecret) {
        VkGroup fromDb = new VkGroup();
        fromDb.setSecretKey(dbSecret);

        Wallpost sourceWallpost = new Wallpost();
        sourceWallpost.setText("Test text");
        sourceWallpost.setDate(0);
        sourceWallpost.setId(2);
        sourceWallpost.setOwnerId(1);

        RawPostDto expected = new RawPostDto();
        expected.setText("Test text");
        expected.setDate(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC));
        expected.setUrl("https://vk.com/club1?w=wall-1_2");
        expected.setContext(new PostContext());

        when(repository.findById(any()))
            .thenReturn(Optional.of(fromDb));

        RawPostDto actual = service.wallpostNew(1, sourceWallpost, acceptedSecret);

        ArgumentCaptor<RawPostDto> wallpostCapture = ArgumentCaptor.forClass(RawPostDto.class);
        verify(template).convertAndSend(wallpostCapture.capture());
        RawPostDto captured = wallpostCapture.getValue();

        assertThat(captured)
            .isNotNull()
            .isNotSameAs(sourceWallpost)
            .isNotSameAs(expected)
            .isSameAs(actual)
            .isEqualToComparingFieldByField(expected);
    }

    @Test
    void wallpostNewGroupNotExist() {
        when(repository.findById(any()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.confirmation(1))
            .isNotNull()
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Group not registered.");
    }


    @ParameterizedTest
    @MethodSource("ru.rassafel.foodsharing.vkparser.service.impl.SecretMatches#registerWithoutAccessNotMatchValues")
    void wallpostNewSecretNotMatch(String dbSecret, String acceptedSecret) {
        VkGroup fromDb = new VkGroup();
        fromDb.setSecretKey(dbSecret);

        Wallpost wallpost = new Wallpost();

        when(repository.findById(any()))
            .thenReturn(Optional.of(fromDb));

        assertThatThrownBy(() -> service.wallpostNew(1, wallpost, acceptedSecret))
            .isNotNull()
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Secret key do not match");
    }

    @Test
    void confirmationGroupExist() {
        VkGroup fromDb = new VkGroup();
        fromDb.setConfirmationCode("Test code");

        String expected = "Test code";

        when(repository.findById(any()))
            .thenReturn(Optional.of(fromDb));

        String actual = service.confirmation(1);

        assertThat(actual)
            .isNotNull()
            .isNotBlank()
            .isEqualTo(expected);
    }

    @Test
    void confirmationGroupNotExist() {
        when(repository.findById(any()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.confirmation(1))
            .isNotNull()
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Group not registered.");
    }
}

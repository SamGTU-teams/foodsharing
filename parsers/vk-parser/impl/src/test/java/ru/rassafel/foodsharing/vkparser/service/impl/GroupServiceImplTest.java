package ru.rassafel.foodsharing.vkparser.service.impl;

import com.vk.api.sdk.client.VkApiClient;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.rassafel.foodsharing.vkparser.config.ApplicationProperties;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;
import ru.rassafel.foodsharing.vkparser.repository.GroupRepository;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author rassafel
 */
class GroupServiceImplTest {
    @InjectMocks
    GroupServiceImpl service;

    @Mock
    GroupRepository repository;

    @Mock
    VkApiClient client;

    @Mock
    ApplicationProperties properties;

    VkGroup accepted;

    VkGroup fromDb;

    private static Stream<Arguments> registerWithoutAccessMatchValues() {
        return Stream.<Arguments>builder()
            .add(Arguments.of(null, null))
            .add(Arguments.of("Test secret", "Test secret"))
            .build();
    }

    private static Stream<Arguments> registerWithoutAccessNotMatchValues() {
        return Stream.<Arguments>builder()
            .add(Arguments.of("Test secret", null))
            .add(Arguments.of(null, "Test secret"))
            .add(Arguments.of("Test secret", "Not match secret"))
            .build();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        when(properties.getUrl()).thenReturn("TestUrl");
        when(properties.getServerTitle()).thenReturn("Test title");
        when(client.getVersion()).thenReturn("5.131");

        accepted = new VkGroup();
        accepted.setGroupId(1);

        fromDb = new VkGroup();
        fromDb.setGroupId(1);
    }

    @Disabled
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Test secret"})
    void registerNewGroupWithAccess(String acceptedSecret) {
        when(repository.findById(any()))
            .thenReturn(Optional.empty());

        accepted.setAccessToken("Test token");
        accepted.setSecretKey(acceptedSecret);

//        ToDo: Mock VkApiClient.

        VkGroup actual = service.registerWithAccess(accepted);

        verify(repository, times(1)).findById(eq(1));
        verify(repository, times(2)).save(any());
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("registerWithoutAccessMatchValues")
    void registerExistGroupWithAccess(String dbSecret, String acceptedSecret) {
        fromDb.setAccessToken("Old token");
        fromDb.setSecretKey(dbSecret);

        when(repository.findById(any()))
            .thenReturn(Optional.of(fromDb));

        accepted.setAccessToken("Test token");
        accepted.setSecretKey(acceptedSecret);

//        ToDo: Mock VkApiClient.

        VkGroup actual = service.registerWithAccess(accepted);

        verify(repository, times(1)).findById(eq(1));
        verify(repository, times(2)).save(any());
    }

    @ParameterizedTest
    @MethodSource("registerWithoutAccessNotMatchValues")
    void registerExistGroupWithAccessSecretNotMatch(String dbSecret, String acceptedSecret) {
        fromDb.setAccessToken("Old token");
        fromDb.setSecretKey(dbSecret);

        when(repository.findById(any()))
            .thenReturn(Optional.of(fromDb));

        accepted.setAccessToken("Test token");
        accepted.setSecretKey(acceptedSecret);

        assertThatThrownBy(() -> service.registerWithAccess(accepted))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Secret key do not match.");

        verify(repository, times(1)).findById(eq(1));
        verify(repository, never()).save(any());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Test secret"})
    void registerNewGroupWithoutAccess(String acceptedSecret) {
        fromDb.setConfirmationCode("Test code");
        fromDb.setSecretKey(acceptedSecret);

        when(repository.findById(any()))
            .thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(fromDb);

        accepted.setConfirmationCode("Test code");
        accepted.setSecretKey(acceptedSecret);

        VkGroup actual = service.registerWithoutAccess(accepted);

        verify(repository, times(1)).findById(eq(1));
        verify(repository, times(1)).save(eq(accepted));

        assertThat(actual)
            .isNotNull()
            .isSameAs(fromDb)
            .isNotSameAs(accepted)
            .isEqualToComparingFieldByField(accepted);
    }

    @ParameterizedTest
    @MethodSource("registerWithoutAccessMatchValues")
    void registerExistGroupWithoutAccess(String dbSecret, String acceptedSecret) {
        fromDb.setConfirmationCode("Old code");
        fromDb.setSecretKey(dbSecret);

        when(repository.findById(any()))
            .thenReturn(Optional.of(fromDb));
        when(repository.save(any())).thenReturn(fromDb);

        accepted.setConfirmationCode("New code");
        accepted.setSecretKey(acceptedSecret);

        VkGroup actual = service.registerWithoutAccess(accepted);

        verify(repository, times(1)).findById(eq(1));
        verify(repository, times(1)).save(eq(accepted));

        assertThat(actual)
            .isNotNull()
            .isSameAs(fromDb)
            .isNotSameAs(accepted)
            .isEqualToComparingFieldByField(accepted);
    }

    @ParameterizedTest
    @MethodSource("registerWithoutAccessNotMatchValues")
    void registerExistGroupWithoutAccessSecretNotMatch(String dbSecret, String acceptedSecret) {
        fromDb.setConfirmationCode("Old code");
        fromDb.setSecretKey(dbSecret);

        when(repository.findById(any()))
            .thenReturn(Optional.of(fromDb));

        accepted.setConfirmationCode("New code");
        accepted.setSecretKey(acceptedSecret);

        assertThatThrownBy(() -> service.registerWithoutAccess(accepted))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Secret key do not match.");

        verify(repository, times(1)).findById(eq(1));
        verify(repository, never()).save(eq(accepted));
    }
}

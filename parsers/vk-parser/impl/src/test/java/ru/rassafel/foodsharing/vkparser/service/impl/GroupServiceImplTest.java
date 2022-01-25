package ru.rassafel.foodsharing.vkparser.service.impl;

import com.vk.api.sdk.actions.Groups;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.base.responses.OkResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Answers;
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
import static org.mockito.AdditionalMatchers.or;
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
    @MethodSource("ru.rassafel.foodsharing.vkparser.service.impl.SecretMatches#registerWithoutAccessMatchValues")
    void registerExistGroupWithAccess(String dbSecret, String acceptedSecret) throws ClientException, ApiException {
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
    @MethodSource("ru.rassafel.foodsharing.vkparser.service.impl.SecretMatches#registerWithoutAccessNotMatchValues")
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
    @MethodSource("ru.rassafel.foodsharing.vkparser.service.impl.SecretMatches#registerWithoutAccessMatchValues")
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
    @MethodSource("ru.rassafel.foodsharing.vkparser.service.impl.SecretMatches#registerWithoutAccessNotMatchValues")
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

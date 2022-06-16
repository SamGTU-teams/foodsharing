package ru.rassafel.foodsharing.vkparser.model.vk.group.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.rassafel.foodsharing.common.exception.ApiException;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;
import ru.rassafel.foodsharing.vkparser.model.vk.group.validator.impl.SecretKeyValidatorImpl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author rassafel
 */
class SecretKeyValidatorTest {
    SecretKeyValidator validator = new SecretKeyValidatorImpl();

    @ParameterizedTest
    @MethodSource("ru.rassafel.foodsharing.vkparser.service.impl.SecretMatches#registerWithoutAccessMatchValues")
    void matchValues(String acceptedSecret, String registeredSecret) {
        VkGroup vkGroup = newGroup(acceptedSecret, 1);
        validator.validate(vkGroup, registeredSecret);
    }

    @ParameterizedTest
    @MethodSource("ru.rassafel.foodsharing.vkparser.service.impl.SecretMatches#registerWithoutAccessNotMatchValues")
    void notMatchValues(String acceptedSecret, String registeredSecret) {
        VkGroup vkGroup = newGroup(acceptedSecret, 1);
        assertThatThrownBy(() -> validator.validate(vkGroup, registeredSecret))
            .isInstanceOf(ApiException.class)
            .hasMessageContaining("Secret key do not match.");
    }

    VkGroup newGroup(String secret, Integer groupId) {
        VkGroup vkGroup = new VkGroup();
        vkGroup.setGroupId(groupId);
        vkGroup.setSecretKey(secret);
        return vkGroup;
    }
}

package ru.rassafel.foodsharing.vkparser.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.rassafel.foodsharing.vkparser.util.GroupUtil.throwIfSecretKeyNotMatch;

/**
 * @author rassafel
 */
class GroupUtilTest {
    @ParameterizedTest
    @MethodSource("ru.rassafel.foodsharing.vkparser.service.impl.SecretMatches#registerWithoutAccessMatchValues")
    void matchValues(String acceptedSecret, String registeredSecret) {
        VkGroup vkGroup = newGroup(acceptedSecret, 1);
        throwIfSecretKeyNotMatch(vkGroup, registeredSecret);
    }

    @ParameterizedTest
    @MethodSource("ru.rassafel.foodsharing.vkparser.service.impl.SecretMatches#registerWithoutAccessNotMatchValues")
    void notMatchValues(String acceptedSecret, String registeredSecret) {
        VkGroup vkGroup = newGroup(acceptedSecret, 1);
        assertThatThrownBy(() -> throwIfSecretKeyNotMatch(vkGroup, registeredSecret))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Secret key do not match.");
    }

    VkGroup newGroup(String secret, Integer groupId) {
        VkGroup vkGroup = new VkGroup();
        vkGroup.setGroupId(groupId);
        vkGroup.setSecretKey(secret);
        return vkGroup;
    }
}

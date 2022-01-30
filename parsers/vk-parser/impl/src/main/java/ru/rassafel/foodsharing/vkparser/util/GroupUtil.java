package ru.rassafel.foodsharing.vkparser.util;

import lombok.extern.slf4j.Slf4j;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;

import java.util.Objects;

/**
 * @author rassafel
 */
@Slf4j
public class GroupUtil {
    private GroupUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void throwIfSecretKeyNotMatch(VkGroup accepted, String registeredSecret) {
        if (!Objects.equals(accepted.getSecretKey(), registeredSecret)) {
            log.warn("Registration for group with id {} declined: Secret key do not match.", accepted.getGroupId());
//            ToDo: Create exception class.
            throw new RuntimeException("Secret key do not match.");
        }
    }
}

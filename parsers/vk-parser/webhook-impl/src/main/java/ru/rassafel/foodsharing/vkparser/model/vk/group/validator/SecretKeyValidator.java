package ru.rassafel.foodsharing.vkparser.model.vk.group.validator;

import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;

/**
 * @author rassafel
 */
public interface SecretKeyValidator {
    void validate(VkGroup group, String secretKey);
}

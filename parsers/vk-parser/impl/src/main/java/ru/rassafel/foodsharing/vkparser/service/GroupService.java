package ru.rassafel.foodsharing.vkparser.service;

import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;

/**
 * @author rassafel
 */
public interface GroupService {
    VkGroup registerWithAccess(VkGroup group);

    VkGroup registerWithoutAccess(VkGroup group);
}

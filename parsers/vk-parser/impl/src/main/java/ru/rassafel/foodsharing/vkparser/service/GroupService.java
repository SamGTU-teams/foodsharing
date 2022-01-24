package ru.rassafel.foodsharing.vkparser.service;

import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;

/**
 * @author rassafel
 */
public interface GroupService {
    void registerWithAccess(VkGroup group);

    void registerWithoutAccess(VkGroup group);
}

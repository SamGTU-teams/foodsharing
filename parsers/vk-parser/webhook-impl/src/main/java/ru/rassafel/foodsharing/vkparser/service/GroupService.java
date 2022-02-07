package ru.rassafel.foodsharing.vkparser.service;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;

/**
 * @author rassafel
 */
public interface GroupService {
    VkGroup registerWithAccess(VkGroup group) throws ClientException, ApiException;

    VkGroup registerWithoutAccess(VkGroup group);
}

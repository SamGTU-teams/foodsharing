package ru.rassafel.foodsharing.vkbot.service;

import ru.rassafel.foodsharing.session.service.UpdateHandler;
import ru.rassafel.foodsharing.vkbot.model.vk.VkUpdate;

/**
 * @author rassafel
 */
public interface VkBotHandler extends UpdateHandler<VkUpdate, String> {
}

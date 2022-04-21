package ru.rassafel.foodsharing.vkparser.service;

import ru.rassafel.foodsharing.parser.model.RawPost;
import ru.rassafel.foodsharing.vkparser.model.vk.Wallpost;

/**
 * @author rassafel
 */
public interface CallbackService {
    RawPost wallpostNew(Integer groupId, Wallpost wallpost, String secret);

    String confirmation(Integer groupId);
}

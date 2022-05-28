package ru.rassafel.foodsharing.vkbot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rassafel.foodsharing.vkbot.model.vk.VkUpdate;

/**
 * @author rassafel
 */
@RequestMapping(CallbackController.MAPPING)
public interface CallbackController {
    String MAPPING = "/callback";
    String OK_MESSAGE = "ok";

    @PostMapping
    ResponseEntity<String> acceptUpdate(@RequestBody VkUpdate update);
}

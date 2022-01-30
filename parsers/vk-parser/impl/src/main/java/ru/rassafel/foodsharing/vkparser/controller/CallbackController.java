package ru.rassafel.foodsharing.vkparser.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rassafel.foodsharing.vkparser.model.vk.CallbackMessage;

/**
 * @author rassafel
 */
@RequestMapping(CallbackController.MAPPING)
public interface CallbackController {
    String MAPPING = "/callback";

    @PostMapping
    ResponseEntity<?> acceptUpdate(@RequestBody CallbackMessage message);
}

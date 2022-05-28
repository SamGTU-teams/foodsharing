package ru.rassafel.foodsharing.tgbot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author rassafel
 */
@RequestMapping(CallbackController.MAPPING)
public interface CallbackController {
    String MAPPING = "/callback";
    String OK_MESSAGE = "{}";

    @PostMapping
    ResponseEntity<String> acceptUpdate(@RequestBody Update update);
}

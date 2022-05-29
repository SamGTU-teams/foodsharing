package ru.rassafel.foodsharing.tgbot.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rassafel.foodsharing.session.repository.CallbackLockRepository;
import ru.rassafel.foodsharing.tgbot.controller.CallbackController;

/**
 * @author rassafel
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class CallbackControllerImpl implements CallbackController {
    private final CallbackLockRepository lockRepository;
    private final RabbitTemplate template;

    @Override
    public ResponseEntity<String> acceptUpdate(Update update) {
        log.debug("Got request from TG: {}", update);
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            Long messageId = update.getMessage().getMessageId().longValue();
            if (!lockRepository.lock(chatId, messageId)) {
                log.debug("Skip message from chat with id = {}", chatId);
                return ResponseEntity.ok(OK_MESSAGE);
            }
        }

        template.convertAndSend(update);
        return ResponseEntity.ok(OK_MESSAGE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handle(HttpMessageNotReadableException ex) {
        log.error("Exception during reading message: {}", ex.getMessage());
        return ResponseEntity.ok("OK");
    }
}

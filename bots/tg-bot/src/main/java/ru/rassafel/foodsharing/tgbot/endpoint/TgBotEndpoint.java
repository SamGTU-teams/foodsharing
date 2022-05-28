package ru.rassafel.foodsharing.tgbot.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rassafel.foodsharing.session.repository.CallbackLockRepository;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/callback")
public class TgBotEndpoint {
    public static final String OK_MESSAGE = "{}";
    private final CallbackLockRepository lockRepository;
    private final RabbitTemplate template;

    @PostMapping
    public ResponseEntity<?> handleUpdate(@RequestBody Update update) {
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
    public ResponseEntity<?> handle(HttpMessageNotReadableException ex){
        log.error("Exception during reading message: {}", ex.getMessage());
        return ResponseEntity.ok("OK");
    }
}

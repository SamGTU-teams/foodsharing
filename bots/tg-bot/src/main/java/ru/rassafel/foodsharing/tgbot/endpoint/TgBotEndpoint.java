package ru.rassafel.foodsharing.tgbot.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.foodsharing.tgbot.service.TgBotHandlerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class TgBotEndpoint {

    private final TgBotHandlerService tgBotHandlerService;

    @PostMapping
    public BotApiMethod<?> handleUpdate(@RequestBody Update update) {
        return tgBotHandlerService.onWebhookUpdateReceived(update);
    }

    @ExceptionHandler(BotException.class)
    public ResponseEntity<SendMessage> handleSessionNotFound(BotException ex) {
        SendMessage message = new SendMessage(ex.getSendTo(), ex.getMessage());
        return ResponseEntity.ok(message);
    }


}

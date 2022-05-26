package ru.rassafel.foodsharing.tgbot.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rassafel.foodsharing.tgbot.service.TgBotHandlerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class TgBotEndpoint {
    private final TgBotHandlerService tgBotHandlerService;

    @PostMapping
    public ResponseEntity<?> handleUpdate(@RequestBody Update update) {
        tgBotHandlerService.onWebhookUpdateReceived(update);
        return ResponseEntity.ok("OK");
    }
}

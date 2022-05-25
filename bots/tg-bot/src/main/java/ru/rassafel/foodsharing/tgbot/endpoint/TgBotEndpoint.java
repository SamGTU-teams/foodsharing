package ru.rassafel.foodsharing.tgbot.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
@RequestMapping("/callback")
public class TgBotEndpoint {
    private final RabbitTemplate template;

    @PostMapping
    public ResponseEntity<?> handleUpdate(@RequestBody Update update) {
        template.convertAndSend(update);
        return ResponseEntity.ok("{}");
    }
}

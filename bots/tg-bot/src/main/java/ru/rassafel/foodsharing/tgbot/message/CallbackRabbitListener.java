package ru.rassafel.foodsharing.tgbot.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rassafel.foodsharing.tgbot.service.TgBotHandlerService;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(queues = {"${spring.rabbitmq.tg-bot-callback.queue}"})
public class CallbackRabbitListener {
    private final TgBotHandlerService tgBotHandlerService;

    @RabbitHandler
    public void receiveMessage(Update update) {
        log.debug("Received object: {}", update);
        tgBotHandlerService.onWebhookUpdateReceived(update);
    }
}

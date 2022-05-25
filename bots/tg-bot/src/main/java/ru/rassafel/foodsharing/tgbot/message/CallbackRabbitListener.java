package ru.rassafel.foodsharing.tgbot.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(queues = {"${spring.rabbitmq.tg-bot-callback.queue}"})
public class CallbackRabbitListener {
    @RabbitHandler
    public void receiveMessage(Object object) {
        log.debug("Received object: {}", object);
    }
}

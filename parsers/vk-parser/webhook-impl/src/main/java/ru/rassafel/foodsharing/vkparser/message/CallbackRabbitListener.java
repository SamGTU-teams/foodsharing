package ru.rassafel.foodsharing.vkparser.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.vkparser.model.vk.CallbackMessage;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(queues = {"${spring.rabbitmq.vk-parser-callback.queue}"})
public class CallbackRabbitListener {
    @RabbitHandler
    public void receiveMessage(CallbackMessage message) {
        log.debug("Received message: {}", message);
    }
}

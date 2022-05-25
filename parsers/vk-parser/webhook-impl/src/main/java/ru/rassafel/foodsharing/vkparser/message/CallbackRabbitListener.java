package ru.rassafel.foodsharing.vkparser.message;

import com.vk.api.sdk.events.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.common.exception.ApiException;
import ru.rassafel.foodsharing.vkparser.model.vk.CallbackMessage;
import ru.rassafel.foodsharing.vkparser.service.CallbackService;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(queues = {"${spring.rabbitmq.vk-parser-callback.queue}"})
public class CallbackRabbitListener {
    private final CallbackService service;

    @RabbitHandler
    public void receiveMessage(CallbackMessage message) {
        log.debug("Received message: {}", message);
        if (Events.WALL_POST_NEW.equals(message.getType())) {
            try {
                service.wallpostNew(message.getGroupId(), message.getWallpost(), message.getSecret());
            } catch (ApiException ex) {
                log.error("Catch exception", ex);
            }
        }
    }
}

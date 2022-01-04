package ru.rassafel.foodsharing.vkbot.message.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author rassafel
 */
@Component
@Slf4j
public class PostListener {
    @RabbitListener(queues = "${spring.rabbitmq.queue}", containerFactory = "rabbitListenerContainerFactory")
    public void listen(String message) {
        log.info("Accepted message: {}", message);
    }
}

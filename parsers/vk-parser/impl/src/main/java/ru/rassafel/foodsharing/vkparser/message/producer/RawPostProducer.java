package ru.rassafel.foodsharing.vkparser.message.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author rassafel
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RawPostProducer {
    private final RabbitTemplate template;

    private final AtomicInteger val = new AtomicInteger();

    @Scheduled(fixedDelay = 1000)
    public void scheduled() {
        String message = "Test message #" + val.addAndGet(1);
        template.convertAndSend(message);
        log.info("Outgoing message: {}", message);
    }
}

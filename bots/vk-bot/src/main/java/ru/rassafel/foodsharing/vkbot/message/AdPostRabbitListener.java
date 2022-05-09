package ru.rassafel.foodsharing.vkbot.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.ad.model.AdPostDto;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(queues = {"spring.rabbit.ad-post.queue"})
public class AdPostRabbitListener {
    private final Validator validator;

    @RabbitHandler
    public void receiveMessage(AdPostDto adPostDto) {
        Set<ConstraintViolation<AdPostDto>> violations = validator.validate(adPostDto);
        if (!violations.isEmpty()) {
            log.debug("Validation exception", new ConstraintViolationException(violations));
            return;
        }
//        ToDo: Send message for all users
    }
}

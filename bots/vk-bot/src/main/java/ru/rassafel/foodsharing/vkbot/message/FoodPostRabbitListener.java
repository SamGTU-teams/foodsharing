package ru.rassafel.foodsharing.vkbot.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.session.service.FoodPostHandlerService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(queues = {"${spring.rabbitmq.ready-post.queue}"})
public class FoodPostRabbitListener {
    private final Validator validator;
    private final FoodPostHandlerService foodPostHandlerService;

    @RabbitHandler
    public void receiveMessage(FoodPostDto foodPostDto) {
        Set<ConstraintViolation<FoodPostDto>> violations = validator.validate(foodPostDto);
        if (!violations.isEmpty()) {
            log.debug("Validation exception", new ConstraintViolationException(violations));
            return;
        }
        foodPostHandlerService.handleFoodPostReceived(foodPostDto);
    }
}

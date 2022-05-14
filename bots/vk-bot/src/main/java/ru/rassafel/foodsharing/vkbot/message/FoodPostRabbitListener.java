package ru.rassafel.foodsharing.vkbot.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.service.FoodPostHandlerService;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.vkbot.repository.VkUserRepository;
import ru.rassafel.foodsharing.vkbot.service.VkMessenger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

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

    @Getter
    private static class ProductsPlacesEntry {
        private final Set<String> productNames = new HashSet<>();
        private final Set<String> placesNames = new HashSet<>();

        public void addEntry(String productName, String placeName){
            productNames.add(productName);
            placesNames.add(placeName);
        }

    }
}

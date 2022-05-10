package ru.rassafel.foodsharing.vkbot.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.vkbot.repository.VkUserRepository;
import ru.rassafel.foodsharing.vkbot.service.VkMessageSchedulerService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(queues = {"${spring.rabbitmq.ready-post.queue}"})
public class FoodPostRabbitListener {
    private final VkUserRepository repository;
    private final VkMessageSchedulerService schedulerService;
    private final Validator validator;

    @RabbitHandler
    public void receiveMessage(FoodPostDto foodPostDto) {
        Set<ConstraintViolation<FoodPostDto>> violations = validator.validate(foodPostDto);
        if (!violations.isEmpty()) {
            log.debug("Validation exception", new ConstraintViolationException(violations));
            return;
        }

        GeoPoint point = foodPostDto.getPoint();
        double lat = point.getLat();
        double lon = point.getLon();
        List<Long> productIds = foodPostDto.getProducts()
            .stream()
            .map(ProductDto::getId)
            .collect(Collectors.toList());
        List<Long> userIds = repository.findByProductAndSuitablePlace(productIds, lat, lon)
            .stream()
            .map(o -> (Long) o[0])
            .collect(Collectors.toList());
        String postText = foodPostDto.getText();
        schedulerService.scheduleEvent(postText, userIds.stream()
            .map(Long::intValue).toArray(Integer[]::new));
    }
}

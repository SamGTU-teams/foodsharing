package ru.rassafel.foodsharing.vkbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.vkbot.repository.VkUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FoodPostConsumer {

    private final VkUserRepository repository;
    private final VkMessageSchedulerService schedulerService;

    public void handleFoodPost(FoodPostDto foodPostDto){
        GeoPoint point = foodPostDto.getPoint();
        double lat = point.getLat();
        double lon = point.getLon();
        List<Long> productIds = foodPostDto.getProducts().stream().map(ProductDto::getId).collect(Collectors.toList());
        List<Long> userIds = repository.findByProductAndSuitablePlace(productIds, lat, lon);
        String postText = foodPostDto.getText();
        schedulerService.scheduleEvent(postText, userIds.stream()
            .map(Long::intValue).toArray(Integer[]::new));
    }

    public void handleAdIntegration(Object ad){

    }

}

package ru.rassafel.foodsharing.tgbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.tgbot.repository.TgUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FoodPostConsumer {

    private final TgUserRepository repository;
    private final TgBotHandlerService tgBotHandlerService;

    public void handleFoodPost(FoodPostDto foodPostDto){
        GeoPoint point = foodPostDto.getPoint();
        double lat = point.getLat();
        double lon = point.getLon();
        List<Long> productIds = foodPostDto.getProducts().stream().map(ProductDto::getId).collect(Collectors.toList());
        List<Long> userIds = repository.findByProductAndSuitablePlace(productIds, lat, lon);
        String postText = foodPostDto.getText();
        tgBotHandlerService.sendEvent(postText, userIds.toArray(Long[]::new));
    }

    public void handleAdIntegration(Object ad){

    }

}

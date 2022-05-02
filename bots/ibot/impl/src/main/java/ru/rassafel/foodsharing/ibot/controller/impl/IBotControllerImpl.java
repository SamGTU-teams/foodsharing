package ru.rassafel.foodsharing.ibot.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPostDto;
import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.common.model.mapper.ProductMapper;
import ru.rassafel.foodsharing.ibot.controller.IBotController;
import ru.rassafel.foodsharing.ibot.model.dto.RequestFoodPostDto;
import ru.rassafel.foodsharing.ibot.model.entity.FoodPost;
import ru.rassafel.foodsharing.ibot.model.mapper.FoodPostMapper;
import ru.rassafel.foodsharing.ibot.service.IBotService;

import java.util.List;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class IBotControllerImpl implements IBotController {
    private final IBotService service;

    private final FoodPostMapper foodPostMapper;

    private final ProductMapper productMapper;

    @Override
    public List<FoodPostDto> findNearbyPosts(RequestFoodPostDto request) {
        List<Product> products = productMapper.dtosToEntities(request.getProducts());
        List<FoodPost> nearbyPosts = service.findNearbyPosts(request.getPoint(),
            request.getRange(), request.getAfter(), products);
        return foodPostMapper.entitiesToDtos(nearbyPosts);
    }
}

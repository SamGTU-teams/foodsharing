package ru.rassafel.foodsharing.ibot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rassafel.foodsharing.analyzer.model.dto.FoodPost;
import ru.rassafel.foodsharing.ibot.model.dto.RequestFoodPostDto;

import java.util.List;

/**
 * @author rassafel
 */
@RequestMapping(IBotController.MAPPING)
public interface IBotController {
    String MAPPING = "/posts";

    @PostMapping
    List<FoodPost> findNearbyPosts(@RequestBody RequestFoodPostDto requestFoodPost);
}

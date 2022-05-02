package ru.rassafel.foodsharing.ibot.service;

import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.ibot.model.entity.FoodPost;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rassafel
 */
public interface IBotService {
    List<FoodPost> findNearbyPosts(GeoPoint point, Integer range,
                                   LocalDateTime after, List<Product> products);
}

package ru.rassafel.foodsharing.ibot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.ibot.model.entity.FoodPost;
import ru.rassafel.foodsharing.ibot.repository.FoodPostRepository;
import ru.rassafel.foodsharing.ibot.service.IBotService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class IBotServiceImpl implements IBotService {
    private final FoodPostRepository repository;

    @Override
    public List<FoodPost> findNearbyPosts(GeoPoint point, Integer range,
                                          LocalDateTime after,
                                          List<Product> products) {
        return repository.findNearbyPosts(point.getLat(), point.getLon(),
            range, after, products.stream().map(Product::getId).collect(Collectors.toList()));
    }
}

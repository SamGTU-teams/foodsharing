package ru.rassafel.foodsharing.session.service;

import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.session.model.entity.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author rassafel
 */
public interface ProductService {
    List<String> findProducts(String text);

    default Map<Integer, String> getUsersProductNamesMap(User user) {
        AtomicInteger counter = new AtomicInteger(1);
        return user.getProducts()
            .stream()
            .map(Product::getName)
            .collect(Collectors.toMap(
                o -> counter.getAndIncrement(),
                o -> o
            ));
    }

    default List<String> getUsersProductNames(User user) {
        return user.getProducts()
            .stream()
            .map(Product::getName)
            .collect(Collectors.toList());
    }
}

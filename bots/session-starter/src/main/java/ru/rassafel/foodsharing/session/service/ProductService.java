package ru.rassafel.foodsharing.session.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.analyzer.controller.ProductAnalyzerController;
import ru.rassafel.foodsharing.analyzer.controller.ProductAnalyzerController.ProductRequest;
import ru.rassafel.foodsharing.analyzer.model.dto.ScoreProductDto;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.session.model.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class ProductService {
    private final ProductAnalyzerController productController;
    @Value("${session.productCountPerRequest:5}")
    private Long productCountPerRequest;

    public List<String> getSimilarToTextProducts(String text) {
        return
            productController
                .parseProducts(new ProductRequest(text), productCountPerRequest)
                .stream()
                .map(ScoreProductDto::getProduct)
                .filter(Objects::nonNull)
                .map(ProductDto::getName)
                .collect(Collectors.toList());
    }

    public Map<Integer, String> getUsersProductNamesMap(User user) {
        AtomicInteger counter = new AtomicInteger(1);
        return user.getProducts()
            .stream()
            .map(Product::getName)
            .collect(Collectors.toMap(
                o -> counter.getAndIncrement(),
                o -> o
            ));
    }

    public List<String> getUsersProductNames(User user) {
        return user.getProducts()
            .stream()
            .map(Product::getName)
            .collect(Collectors.toList());
    }
}

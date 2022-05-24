package ru.rassafel.bot.session.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.foodsharing.analyzer.controller.ProductAnalyzerController;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import java.util.List;
import java.util.Map;
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
            productController.parseProducts(text, productCountPerRequest).stream()
                .filter(dto -> dto.getProduct() != null)
                .map(dto -> dto.getProduct().getName())
                .collect(Collectors.toList());
    }

    public Map<Integer, String> getUsersProductNamesMap(User user) {
        AtomicInteger counter = new AtomicInteger(1);
        return user.getProducts().stream().map(Product::getName).collect(Collectors.toMap(
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

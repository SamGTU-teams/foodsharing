package ru.rassafel.foodsharing.session.service.product;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.analyzer.controller.ProductAnalyzerController;
import ru.rassafel.foodsharing.analyzer.controller.ProductAnalyzerController.ProductRequest;
import ru.rassafel.foodsharing.analyzer.model.dto.ScoreProductDto;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.session.service.ProductService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class ProductServiceImpl implements ProductService {
    private final ProductAnalyzerController productController;
    @Value("${session.productCountPerRequest:5}")
    private Long productCountPerRequest;

    @Override
    public List<String> findProducts(String text) {
        return
            productController
                .parseProducts(new ProductRequest(text), productCountPerRequest)
                .stream()
                .map(ScoreProductDto::getProduct)
                .filter(Objects::nonNull)
                .map(ProductDto::getName)
                .collect(Collectors.toList());
    }
}

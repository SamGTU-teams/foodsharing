package ru.rassafel.foodsharing.analyzer.controller.stub;

import ru.rassafel.foodsharing.analyzer.controller.ProductAnalyzerController;
import ru.rassafel.foodsharing.analyzer.model.dto.ScoreProductDto;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;

import java.util.List;

public class ProductAnalyzerControllerStub implements ProductAnalyzerController {
    @Override
    public List<ScoreProductDto> parseProducts(String text, Long count) {
        return List.of(
            new ScoreProductDto(new ProductDto(1L, "Молоко"), 1F),
            new ScoreProductDto(new ProductDto(2L, "Ряженка"), 0.9F),
            new ScoreProductDto(new ProductDto(3L, "Кефир"), 0.8F),
            new ScoreProductDto(new ProductDto(4L, "Шоколад"), 0.8F),
            new ScoreProductDto(new ProductDto(5L, "Конфеты"), 0.8F)
        );
    }
}

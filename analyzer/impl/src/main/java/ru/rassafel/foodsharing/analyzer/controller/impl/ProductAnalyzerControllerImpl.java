package ru.rassafel.foodsharing.analyzer.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.RestController;
import ru.rassafel.foodsharing.analyzer.controller.ProductAnalyzerController;
import ru.rassafel.foodsharing.analyzer.model.dto.ScoreProductDto;
import ru.rassafel.foodsharing.analyzer.service.ProductAnalyzerService;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.common.model.mapper.ProductMapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class ProductAnalyzerControllerImpl implements ProductAnalyzerController {
    private static final Comparator<Pair<Product, Float>> comparator = Comparator.comparing(Pair::getSecond);
    private final ProductAnalyzerService service;
    private final ProductMapper mapper;

    @Override
    public List<ScoreProductDto> parseProducts(String text, Long count) {
        return StreamSupport
            .stream(service.parseProducts(text).spliterator(), false)
            .sorted(comparator.reversed())
            .limit(count)
            .map(p -> new ScoreProductDto(mapper.entityToDto(p.getFirst()), p.getSecond()))
            .collect(Collectors.toList());
    }
}

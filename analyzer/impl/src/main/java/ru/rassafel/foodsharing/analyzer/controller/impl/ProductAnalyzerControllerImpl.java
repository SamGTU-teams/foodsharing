package ru.rassafel.foodsharing.analyzer.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Streamable;
import org.springframework.web.bind.annotation.RestController;
import ru.rassafel.foodsharing.analyzer.controller.ProductAnalyzerController;
import ru.rassafel.foodsharing.analyzer.model.ScoreProduct;
import ru.rassafel.foodsharing.analyzer.model.dto.ScoreProductDto;
import ru.rassafel.foodsharing.analyzer.model.mapper.ScoreProductMapper;
import ru.rassafel.foodsharing.analyzer.service.ProductAnalyzerService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class ProductAnalyzerControllerImpl implements ProductAnalyzerController {
    private static final Comparator<ScoreProduct> comparator = Comparator.comparing(ScoreProduct::getScore);
    private final ProductAnalyzerService service;
    private final ScoreProductMapper mapper;

    @Override
    public List<ScoreProductDto> parseProducts(String text, Long count) {
        return Streamable.of(service.parseProducts(text))
            .stream()
            .sorted(comparator.reversed())
            .limit(count)
            .map(mapper::entityToDto)
            .collect(Collectors.toList());
    }
}

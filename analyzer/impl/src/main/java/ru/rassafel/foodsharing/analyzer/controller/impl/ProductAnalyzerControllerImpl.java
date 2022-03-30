package ru.rassafel.foodsharing.analyzer.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.RestController;
import ru.rassafel.foodsharing.analyzer.controller.ProductAnalyzerController;
import ru.rassafel.foodsharing.analyzer.service.ProductAnalyzerService;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;
import ru.rassafel.foodsharing.common.model.mapper.ProductMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class ProductAnalyzerControllerImpl implements ProductAnalyzerController {
    private final ProductAnalyzerService service;
    private final ProductMapper mapper;

    @Override
    public List<ProductDto> parseProducts(String text) {
        return service.parseProducts(text)
            .map(Pair::getFirst)
            .map(mapper::entityToDto)
            .collect(Collectors.toList());
    }
}

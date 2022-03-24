package ru.rassafel.foodsharing.analyzer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rassafel.foodsharing.common.model.dto.ProductDto;

import java.util.List;

/**
 * @author rassafel
 */
@RequestMapping(ProductAnalyzerController.MAPPING)
public interface ProductAnalyzerController {
    String MAPPING = "/products";

    @PostMapping
    List<ProductDto> parseProducts(@RequestBody String text);
}

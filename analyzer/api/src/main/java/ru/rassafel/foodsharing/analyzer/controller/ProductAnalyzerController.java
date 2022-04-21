package ru.rassafel.foodsharing.analyzer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rassafel.foodsharing.analyzer.model.dto.ScoreProductDto;

import java.util.List;

/**
 * @author rassafel
 */
@RequestMapping(ProductAnalyzerController.MAPPING)
public interface ProductAnalyzerController {
    String MAPPING = "/products";

    @PostMapping
    List<ScoreProductDto> parseProducts(@RequestBody String text,
                                        @RequestParam(name = "count", defaultValue = "" + Long.MAX_VALUE) Long count);
}

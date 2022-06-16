package ru.rassafel.foodsharing.analyzer.service;

import ru.rassafel.foodsharing.analyzer.model.ScoreProduct;
import ru.rassafel.foodsharing.parser.model.dto.RawPostDto;

/**
 * @author rassafel
 */
public interface ProductAnalyzerService {
    Iterable<ScoreProduct> parseProducts(RawPostDto post);

    Iterable<ScoreProduct> parseProducts(String... strings);
}

package ru.rassafel.foodsharing.analyzer.service;

import ru.rassafel.foodsharing.analyzer.model.ScoreProduct;
import ru.rassafel.foodsharing.parser.model.RawPost;

/**
 * @author rassafel
 */
public interface ProductAnalyzerService {
    Iterable<ScoreProduct> parseProducts(RawPost post);

    Iterable<ScoreProduct> parseProducts(String... strings);
}

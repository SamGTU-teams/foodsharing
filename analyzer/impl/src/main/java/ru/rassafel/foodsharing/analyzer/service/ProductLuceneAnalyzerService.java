package ru.rassafel.foodsharing.analyzer.service;

import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.analyzer.model.ScoreProduct;
import ru.rassafel.foodsharing.parser.model.dto.RawPostDto;

/**
 * @author rassafel
 */
public interface ProductLuceneAnalyzerService extends ProductAnalyzerService {
    Iterable<ScoreProduct> parseProducts(RawPostDto post, LuceneIndexedString... indexedStrings);

    Iterable<ScoreProduct> parseProducts(LuceneIndexedString... indexedStrings);
}

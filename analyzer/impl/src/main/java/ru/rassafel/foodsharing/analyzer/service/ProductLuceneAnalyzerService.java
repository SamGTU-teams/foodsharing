package ru.rassafel.foodsharing.analyzer.service;

import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.analyzer.model.ScoreProduct;
import ru.rassafel.foodsharing.parser.model.RawPost;

/**
 * @author rassafel
 */
public interface ProductLuceneAnalyzerService extends ProductAnalyzerService {
    Iterable<ScoreProduct> parseProducts(RawPost post, LuceneIndexedString... indexedStrings);

    Iterable<ScoreProduct> parseProducts(LuceneIndexedString... indexedStrings);
}

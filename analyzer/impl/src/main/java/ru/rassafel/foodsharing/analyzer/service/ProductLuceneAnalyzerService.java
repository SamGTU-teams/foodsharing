package ru.rassafel.foodsharing.analyzer.service;

import org.springframework.data.util.Pair;
import org.springframework.data.util.Streamable;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.parser.model.RawPost;

/**
 * @author rassafel
 */
public interface ProductLuceneAnalyzerService extends ProductAnalyzerService {
    Streamable<Pair<Product, Float>> parseProducts(RawPost post, LuceneIndexedString... indexedStrings);

    Streamable<Pair<Product, Float>> parseProducts(LuceneIndexedString... indexedStrings);
}

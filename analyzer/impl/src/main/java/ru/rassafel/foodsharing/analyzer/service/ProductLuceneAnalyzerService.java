package ru.rassafel.foodsharing.analyzer.service;

import org.springframework.data.util.Pair;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.parser.model.RawPost;

/**
 * @author rassafel
 */
public interface ProductLuceneAnalyzerService extends ProductAnalyzerService {
    Iterable<Pair<Product, Float>> parseProducts(RawPost post, LuceneIndexedString... indexedStrings);

    Iterable<Pair<Product, Float>> parseProducts(LuceneIndexedString... indexedStrings);
}

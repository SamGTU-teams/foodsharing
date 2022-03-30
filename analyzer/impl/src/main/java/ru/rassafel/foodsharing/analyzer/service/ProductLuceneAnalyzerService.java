package ru.rassafel.foodsharing.analyzer.service;

import org.springframework.data.util.Pair;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.parser.model.RawPost;

import java.util.stream.Stream;

/**
 * @author rassafel
 */
public interface ProductLuceneAnalyzerService extends ProductAnalyzerService {
    Stream<Pair<Product, Float>> parseProducts(RawPost post, LuceneIndexedString... indexedStrings);

    Stream<Pair<Product, Float>> parseProducts(LuceneIndexedString... indexedStrings);
}

package ru.rassafel.foodsharing.analyzer.service;

import org.springframework.data.util.Pair;
import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.parser.model.RawPost;

/**
 * @author rassafel
 */
public interface ProductAnalyzerService {
    Iterable<Pair<Product, Float>> parseProducts(RawPost post);

    Iterable<Pair<Product, Float>> parseProducts(String... strings);
}

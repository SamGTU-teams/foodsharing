package ru.rassafel.foodsharing.analyzer.service;

import org.springframework.data.util.Pair;
import org.springframework.data.util.Streamable;
import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.parser.model.RawPost;

/**
 * @author rassafel
 */
public interface ProductAnalyzerService {
    Streamable<Pair<Product, Float>> parseProducts(RawPost post);

    Streamable<Pair<Product, Float>> parseProducts(String... strings);
}

package ru.rassafel.foodsharing.analyzer.service;

import org.springframework.data.util.Pair;
import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.parser.model.RawPost;

import java.util.stream.Stream;

/**
 * @author rassafel
 */
public interface ProductAnalyzerService {
    Stream<Pair<Product, Float>> parseProducts(RawPost post);

    Stream<Pair<Product, Float>> parseProducts(String... strings);
}

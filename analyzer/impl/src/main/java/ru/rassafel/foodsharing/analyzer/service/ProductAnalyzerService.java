package ru.rassafel.foodsharing.analyzer.service;

import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.parser.model.RawPost;

import java.util.List;

/**
 * @author rassafel
 */
public interface ProductAnalyzerService {
    List<Product> parseProducts(RawPost post);

    List<Product> parseProducts(String... strings);
}

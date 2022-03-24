package ru.rassafel.foodsharing.analyzer.service;

import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.parser.model.RawPost;

import java.util.List;

/**
 * @author rassafel
 */
public interface ProductLuceneAnalyzerService extends ProductAnalyzerService {
    List<Product> parseProducts(RawPost post, LuceneIndexedString... strings);

    List<Product> parseProducts(LuceneIndexedString... strings);
}

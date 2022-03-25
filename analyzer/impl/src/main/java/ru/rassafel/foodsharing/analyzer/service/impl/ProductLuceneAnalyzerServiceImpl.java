package ru.rassafel.foodsharing.analyzer.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.FuzzyQuery;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.analyzer.repository.LuceneRepository;
import ru.rassafel.foodsharing.analyzer.repository.ProductRepository;
import ru.rassafel.foodsharing.analyzer.service.ProductLuceneAnalyzerService;
import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.parser.model.RawPost;

import java.util.Arrays;
import java.util.List;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ProductLuceneAnalyzerServiceImpl implements ProductLuceneAnalyzerService {
    private final ProductRepository productRepository;
    private final LuceneRepository luceneRepository;
    private final RequestParams params = new RequestParams();

    @Override
    public List<Product> parseProducts(RawPost post) {
        return parseProducts(post.getText());
    }

    @Override
    public List<Product> parseProducts(String... strings) {
        LuceneIndexedString[] indexedStrings = Arrays.stream(strings).map(luceneRepository::create).toArray(LuceneIndexedString[]::new);
        return parseProducts(indexedStrings);
    }

    @Override
    public List<Product> parseProducts(RawPost post, LuceneIndexedString... indexedStrings) {
        return parseProducts(indexedStrings);
    }

    @Override
    public List<Product> parseProducts(LuceneIndexedString... indexedStrings) {
        return List.of();
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class RequestParams {
        public int pageSize = 100;
        public Integer maxEdits = FuzzyQuery.defaultMaxEdits;
        public int prefixLength = FuzzyQuery.defaultPrefixLength;
        public int maxExpansions = FuzzyQuery.defaultMaxExpansions;
        public boolean transpositions = FuzzyQuery.defaultTranspositions;
        public float minimumSimilarity = FuzzyQuery.defaultMinSimilarity;
    }
}

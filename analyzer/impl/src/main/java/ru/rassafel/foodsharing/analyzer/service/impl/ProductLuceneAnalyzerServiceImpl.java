package ru.rassafel.foodsharing.analyzer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.springframework.data.util.Pair;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.analyzer.config.LuceneProperties;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.analyzer.model.ScoreProduct;
import ru.rassafel.foodsharing.analyzer.repository.LuceneRepository;
import ru.rassafel.foodsharing.analyzer.repository.ProductRepository;
import ru.rassafel.foodsharing.analyzer.service.ProductLuceneAnalyzerService;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
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
    private final LuceneProperties params;

    @Override
    public List<ScoreProduct> parseProducts(RawPost post) {
        return parseProducts(post.getText());
    }

    @Override
    public List<ScoreProduct> parseProducts(String... strings) {
        LuceneIndexedString[] indexedStrings = Arrays.stream(strings)
            .map(luceneRepository::add)
            .toArray(LuceneIndexedString[]::new);
        List<ScoreProduct> result = parseProducts(indexedStrings);
        luceneRepository.unregisterAll(List.of(indexedStrings));
        return result;
    }

    @Override
    public List<ScoreProduct> parseProducts(RawPost post, LuceneIndexedString... indexedStrings) {
        return parseProducts(indexedStrings);
    }

    @Override
    public List<ScoreProduct> parseProducts(LuceneIndexedString... indexedStrings) {
        Query byIdQuery = findByIdQuery(indexedStrings);

        return productRepository.findAll()
            .map(product -> parseProduct(product, byIdQuery))
            .filter(pair -> pair.getScore() > 0f)
            .toList();
    }

    ScoreProduct parseProduct(Product product, Query findByIdQuery) {
        Query fizzyQuery = fizzyQuery(product.getName());
        BooleanQuery query = new BooleanQuery.Builder()
            .add(fizzyQuery, BooleanClause.Occur.MUST)
            .add(findByIdQuery, BooleanClause.Occur.MUST)
            .build();

        int maxResults = params.getLuceneMaxResults();

        return Streamable.of(luceneRepository.search(query, maxResults))
            .map(Pair::getSecond)
            .stream()
            .max(Float::compare)
            .map(f -> new ScoreProduct(f, product))
            .orElseGet(() -> new ScoreProduct(0f, product));
    }

    BooleanQuery findByIdQuery(LuceneIndexedString... indexedStrings) {
        BooleanQuery.Builder findByIdBuilder = new BooleanQuery.Builder();
        Arrays.stream(indexedStrings)
            .map(s -> new Term(LuceneRepository.FIELD_ID, s.getId()))
            .map(TermQuery::new)
            .map(q -> new BooleanClause(q, BooleanClause.Occur.SHOULD))
            .forEachOrdered(findByIdBuilder::add);
        return findByIdBuilder.build();
    }

    FuzzyQuery fizzyQuery(String text) {
        return new FuzzyQuery(new Term(LuceneRepository.FIELD_BODY, text),
            params.getMaxEdits(),
            params.getPrefixLength(),
            params.getMaxExpansions(),
            params.isTranspositions());
    }
}

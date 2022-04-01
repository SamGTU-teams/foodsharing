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
import ru.rassafel.foodsharing.analyzer.repository.LuceneRepository;
import ru.rassafel.foodsharing.analyzer.repository.ProductRepository;
import ru.rassafel.foodsharing.analyzer.service.ProductLuceneAnalyzerService;
import ru.rassafel.foodsharing.common.model.entity.Product;
import ru.rassafel.foodsharing.parser.model.RawPost;

import java.util.Arrays;

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
    public Streamable<Pair<Product, Float>> parseProducts(RawPost post) {
        return parseProducts(post.getText());
    }

    @Override
    public Streamable<Pair<Product, Float>> parseProducts(String... strings) {
        LuceneIndexedString[] indexedStrings = Arrays.stream(strings)
            .map(luceneRepository::add)
            .toArray(LuceneIndexedString[]::new);
        return parseProducts(indexedStrings);
    }

    @Override
    public Streamable<Pair<Product, Float>> parseProducts(RawPost post, LuceneIndexedString... indexedStrings) {
        return parseProducts(indexedStrings);
    }

    @Override
    public Streamable<Pair<Product, Float>> parseProducts(LuceneIndexedString... indexedStrings) {
        Query byIdQuery = findByIdQuery(indexedStrings);

        return productRepository.findAll()
            .map(product -> parseProduct(product, byIdQuery))
            .filter(pair -> pair.getSecond() > 0);
    }

    Pair<Product, Float> parseProduct(Product product, Query findByIdQuery) {
        Query fizzyQuery = fizzyQuery(product.getName());
        BooleanQuery query = new BooleanQuery.Builder()
            .add(fizzyQuery, BooleanClause.Occur.MUST)
            .add(findByIdQuery, BooleanClause.Occur.MUST)
            .build();

        float maxScore = (float) luceneRepository.search(query, params.getLuceneMaxResults())
            .stream()
            .mapToDouble(Pair::getSecond)
            .max()
            .orElse(0);
        return Pair.of(product, maxScore);
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

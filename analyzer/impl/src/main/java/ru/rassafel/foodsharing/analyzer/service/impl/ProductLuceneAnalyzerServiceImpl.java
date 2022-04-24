package ru.rassafel.foodsharing.analyzer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.analyzer.config.LuceneProperties;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.analyzer.repository.LuceneRepository;
import ru.rassafel.foodsharing.analyzer.repository.ProductRepository;
import ru.rassafel.foodsharing.analyzer.service.ProductLuceneAnalyzerService;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.parser.model.RawPost;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

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
    public List<Pair<Product, Float>> parseProducts(RawPost post) {
        return parseProducts(post.getText());
    }

    @Override
    public List<Pair<Product, Float>> parseProducts(String... strings) {
        LuceneIndexedString[] indexedStrings = Arrays.stream(strings)
            .map(luceneRepository::add)
            .toArray(LuceneIndexedString[]::new);
        List<Pair<Product, Float>> result = parseProducts(indexedStrings);
        luceneRepository.unregisterAll(List.of(indexedStrings));
        return result;
    }

    @Override
    public List<Pair<Product, Float>> parseProducts(RawPost post, LuceneIndexedString... indexedStrings) {
        return parseProducts(indexedStrings);
    }

    @Override
    public List<Pair<Product, Float>> parseProducts(LuceneIndexedString... indexedStrings) {
        Query byIdQuery = findByIdQuery(indexedStrings);

        return productRepository.findAll()
            .map(product -> parseProduct(product, byIdQuery))
            .filter(pair -> pair.getSecond() > 0)
            .toList();
    }

    Pair<Product, Float> parseProduct(Product product, Query findByIdQuery) {
        Query fizzyQuery = fizzyQuery(product.getName());
        BooleanQuery query = new BooleanQuery.Builder()
            .add(fizzyQuery, BooleanClause.Occur.MUST)
            .add(findByIdQuery, BooleanClause.Occur.MUST)
            .build();

        int maxResults = params.getLuceneMaxResults();

        float maxScore = StreamSupport
            .stream(luceneRepository.search(query, maxResults).spliterator(), false)
            .map(Pair::getSecond)
            .max(Float::compare)
            .orElse(0f);
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

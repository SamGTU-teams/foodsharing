package ru.rassafel.foodsharing.analyzer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.analyzer.config.LuceneProperties;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.analyzer.model.ScoreProduct;
import ru.rassafel.foodsharing.analyzer.repository.LuceneRepository;
import ru.rassafel.foodsharing.analyzer.repository.ProductRepository;
import ru.rassafel.foodsharing.analyzer.service.ProductLuceneAnalyzerService;
import ru.rassafel.foodsharing.common.model.entity.product.Product;
import ru.rassafel.foodsharing.parser.model.dto.RawPostDto;

import java.util.Arrays;
import java.util.Collection;
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
    public List<ScoreProduct> parseProducts(RawPostDto post) {
        return parseProducts(post.getText());
    }

    @Override
    public List<ScoreProduct> parseProducts(String... strings) {
        LuceneIndexedString[] indexedStrings = Arrays.stream(strings)
            .map(String::toLowerCase)
            .map(luceneRepository::add)
            .toArray(LuceneIndexedString[]::new);
        List<ScoreProduct> result = parseProducts(indexedStrings);
        luceneRepository.unregisterAll(List.of(indexedStrings));
        return result;
    }

    @Override
    public List<ScoreProduct> parseProducts(RawPostDto post, LuceneIndexedString... indexedStrings) {
        return parseProducts(indexedStrings);
    }

    @Override
    public List<ScoreProduct> parseProducts(LuceneIndexedString... indexedStrings) {
        List<String> ids = Streamable.of(indexedStrings)
            .map(LuceneIndexedString::getId)
            .toList();
        return productRepository.findAll()
            .map(this::parseProduct)
            .flatMap(Collection::stream)
            .filter(pair -> ids.contains(pair.getLeft().getId()))
            .filter(pair -> pair.getRight().getScore() > 0)
            .map(Pair::getRight)
            .toList();
    }

    List<Pair<LuceneIndexedString, ScoreProduct>> parseProduct(Product product) {
        Query query = fizzyQuery(product.getName());

        int maxResults = params.getLuceneMaxResults();

        return Streamable.of(luceneRepository.search(query, maxResults))
            .map(pair -> Pair.of(pair.getLeft(), new ScoreProduct(pair.getRight(), product)))
            .toList();
    }

    FuzzyQuery fizzyQuery(String text) {
        return new FuzzyQuery(new Term(LuceneRepository.FIELD_BODY, text.toLowerCase()),
            params.getMaxEdits(),
            params.getPrefixLength(),
            params.getMaxExpansions(),
            params.isTranspositions());
    }
}

package ru.rassafel.foodsharing.analyzer.service.impl;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.util.Pair;
import org.springframework.data.util.Streamable;
import ru.rassafel.foodsharing.analyzer.config.LuceneProperties;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;
import ru.rassafel.foodsharing.analyzer.repository.ProductRepository;
import ru.rassafel.foodsharing.analyzer.repository.impl.LuceneRepositoryImpl;
import ru.rassafel.foodsharing.common.model.entity.product.Product;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author rassafel
 */
class ProductLuceneAnalyzerServiceImplTest {
    @Spy
    LuceneProperties luceneProperties;
    @Spy
    LuceneRepositoryImpl luceneRepository;
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductLuceneAnalyzerServiceImpl service;

    @BeforeEach
    void setUp() throws IOException {
        luceneProperties = new LuceneProperties();
        Directory directory = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);
        SearcherManager searcherManager = new SearcherManager(writer, false, false, null);
        luceneRepository = new LuceneRepositoryImpl(searcherManager, writer);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void parseProducts() {
        luceneRepository.registerAll(List.of(
            new LuceneIndexedString("sync1", "Test string with orange"),
            new LuceneIndexedString("sync2", "Test string with apple")));

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("apple");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("orange");

        Product product3 = new Product();
        product3.setId(3L);
        product3.setName("peach");

        when(productRepository.findAll())
            .thenReturn(Streamable.of(product1, product2, product3));

        List<Pair<Product, Float>> actual = service.parseProducts("Test post contains products like: banana, orenge, peach");

        System.out.println(actual);

        assertThat(actual.stream()
            .map(Pair::getFirst)
            .collect(Collectors.toList()))
            .contains(product2)
            .contains(product3)
            .doesNotContain(product1);
    }
}

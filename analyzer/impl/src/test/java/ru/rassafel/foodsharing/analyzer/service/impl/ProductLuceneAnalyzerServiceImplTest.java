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
import ru.rassafel.foodsharing.analyzer.repository.ProductRepository;
import ru.rassafel.foodsharing.analyzer.repository.impl.LuceneRepositoryImpl;
import ru.rassafel.foodsharing.common.model.entity.Product;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author rassafel
 */
class ProductLuceneAnalyzerServiceImplTest {
    @Spy
    LuceneRepositoryImpl luceneRepository;
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductLuceneAnalyzerServiceImpl service;

    @BeforeEach
    void setUp() throws IOException {
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
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("apple");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("orange");
        when(productRepository.findByNameIsNotNull())
            .thenReturn(Stream.of(product1, product2));

        List<Product> actual = service.parseProducts("Test post contains products like: banana, orenge")
            .map(Pair::getFirst)
            .collect(Collectors.toList());

        assertThat(actual)
            .contains(product2)
            .doesNotContain(product1);
    }
}

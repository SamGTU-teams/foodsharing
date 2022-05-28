package ru.rassafel.foodsharing.analyzer.repository.impl;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rassafel
 */
class LuceneRepositoryImplTest {
    LuceneRepositoryImpl repository;

    @BeforeEach
    @SuppressWarnings("deprecation")
    void setUp() throws IOException {
        Directory directory = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);
        SearcherManager searcherManager = new SearcherManager(writer, false, false, null);
        repository = new LuceneRepositoryImpl(searcherManager, writer);
    }

    @SneakyThrows
    LuceneIndexedString createFirstObject(String body) {
        assertThat(repository.findAll(100))
            .isEmpty();

        LuceneIndexedString result = repository.add(body);

        assertThat(result)
            .isNotNull();
        assertThat(result.getBody())
            .isEqualTo(body);
        assertThat(result.getId())
            .isNotEmpty();

        assertThat(repository.findAll(100))
            .isNotEmpty();

        return result;
    }

    @Test
    void create() {
        String body = "Test value for create object.";
        LuceneIndexedString object = createFirstObject(body);
    }

    @Test
    void delete() {
        String body = "Test value for create object.";
        LuceneIndexedString object = createFirstObject(body);
        repository.unregister(object);
        assertThat(repository.findAll(100))
            .isEmpty();
    }

    @Test
    void search() {
        String body = "Test value for create object.";
        LuceneIndexedString object = createFirstObject(body);
        Stream.generate(() -> RandomStringUtils.randomAlphabetic(16))
            .limit(10)
            .forEach(text -> repository.add(text));

        TermQuery query = new TermQuery(new Term(LuceneRepositoryImpl.FIELD_ID, object.getId()));
        List<Pair<LuceneIndexedString, Float>> result = repository.search(query, 100);

        assertThat(result)
            .hasSize(1);

        LuceneIndexedString actual = result.get(0).getLeft();

        assertThat(actual)
            .isNotNull();
        assertThat(actual.getId())
            .isEqualTo(object.getId());
    }

    @Test
    void findAll() {
        int count = 10;
        Stream.generate(() -> RandomStringUtils.randomAlphabetic(16))
            .limit(count)
            .forEach(text -> repository.add(text));
        List<LuceneIndexedString> actual = repository.findAll(count);
        assertThat(actual)
            .hasSize(count);
    }
}

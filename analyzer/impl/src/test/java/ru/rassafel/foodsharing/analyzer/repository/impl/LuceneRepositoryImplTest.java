package ru.rassafel.foodsharing.analyzer.repository.impl;

import lombok.SneakyThrows;
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
    Directory directory;
    SearcherManager searcherManager;

    @BeforeEach
    void setUp() throws IOException {
        directory = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);
        searcherManager = new SearcherManager(writer, false, false, null);
        repository = new LuceneRepositoryImpl(searcherManager, writer, 100);
    }

    @SneakyThrows
    LuceneIndexedString createFirstObject(String body) {
        assertThat(repository.findAll())
            .isEmpty();

        LuceneIndexedString result = repository.create(body);

        assertThat(result)
            .isNotNull();
        assertThat(result.getBody())
            .isEqualTo(body);
        assertThat(result.getId())
            .isNotEmpty();

        assertThat(repository.findAll())
            .isNotEmpty();

        return result;
    }

    @Test
    void create() throws IOException {
        String body = "Test value for create object.";
        LuceneIndexedString object = createFirstObject(body);
    }

    @Test
    void delete() throws IOException {
        String body = "Test value for create object.";
        LuceneIndexedString object = createFirstObject(body);
        repository.delete(object);
        assertThat(repository.findAll())
            .isEmpty();
    }

    @Test
    void search() {
        String body = "Test value for create object.";
        LuceneIndexedString object = createFirstObject(body);
        Stream.generate(() -> RandomStringUtils.randomAlphabetic(16))
            .limit(10)
            .forEach(text -> repository.create(text));

        TermQuery query = new TermQuery(new Term(LuceneRepositoryImpl.FIELD_ID, object.getId()));
        List<LuceneIndexedString> result = repository.search(query);

        assertThat(result)
            .hasSize(1);

        LuceneIndexedString actual = result.get(0);

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
            .forEach(text -> repository.create(text));
        List<LuceneIndexedString> actual = repository.findAll(count);
        assertThat(actual)
            .hasSize(count);
    }
}

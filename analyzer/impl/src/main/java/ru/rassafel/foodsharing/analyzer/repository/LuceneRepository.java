package ru.rassafel.foodsharing.analyzer.repository;

import org.apache.lucene.search.Query;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;

import java.util.List;

/**
 * @author rassafel
 */
public interface LuceneRepository {
    LuceneIndexedString create(String body);

    void delete(String id);

    default void delete(LuceneIndexedString object) {
        delete(object.getId());
    }

    List<LuceneIndexedString> search(Query query, int count);

    List<LuceneIndexedString> search(Query query);

    List<LuceneIndexedString> findAll(int count);

    List<LuceneIndexedString> findAll();
}

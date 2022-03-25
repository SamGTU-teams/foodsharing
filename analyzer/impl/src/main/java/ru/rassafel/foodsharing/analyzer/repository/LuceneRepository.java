package ru.rassafel.foodsharing.analyzer.repository;

import org.apache.lucene.search.Query;
import org.springframework.data.repository.NoRepositoryBean;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;

import java.util.List;

/**
 * @author rassafel
 */
@NoRepositoryBean
public interface LuceneRepository {
    String FIELD_ID = "id";
    String FIELD_BODY = "body";

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

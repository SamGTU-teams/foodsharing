package ru.rassafel.foodsharing.analyzer.repository;

import org.apache.lucene.search.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.util.Pair;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;

import java.util.List;

/**
 * @author rassafel
 */
@NoRepositoryBean
public interface LuceneRepository {
    String FIELD_ID = "id";
    String FIELD_BODY = "body";

    LuceneIndexedString add(String body);

    void delete(String id);

    default void delete(LuceneIndexedString object) {
        delete(object.getId());
    }

    List<Pair<LuceneIndexedString, Float>> search(Query query, int count);

    List<LuceneIndexedString> findAll(int count);
}

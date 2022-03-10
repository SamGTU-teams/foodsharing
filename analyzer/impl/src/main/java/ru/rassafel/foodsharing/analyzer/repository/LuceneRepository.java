package ru.rassafel.foodsharing.analyzer.repository;

import org.apache.lucene.search.Query;
import ru.rassafel.foodsharing.analyzer.model.LuceneObject;

import java.util.List;

/**
 * @author rassafel
 */
public interface LuceneRepository {
    LuceneObject create(String body);

    void delete(String id);

    default void delete(LuceneObject object) {
        delete(object.getId());
    }

    List<LuceneObject> search(Query query, int count);

    List<LuceneObject> search(Query query);

    List<LuceneObject> findAll(int count);

    List<LuceneObject> findAll();
}

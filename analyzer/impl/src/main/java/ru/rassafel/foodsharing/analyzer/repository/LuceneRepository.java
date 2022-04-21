package ru.rassafel.foodsharing.analyzer.repository;

import org.apache.lucene.search.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.util.Pair;
import ru.rassafel.foodsharing.analyzer.model.LuceneIndexedString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * @author rassafel
 */
@NoRepositoryBean
public interface LuceneRepository {
    String FIELD_ID = "id";
    String FIELD_BODY = "body";

    Iterable<LuceneIndexedString> addAll(Iterable<String> strings);

    default LuceneIndexedString add(String string) {
        return addAll(List.of(string)).iterator().next();
    }

    void registerAll(Iterable<LuceneIndexedString> strings);

    default void register(LuceneIndexedString string) {
        registerAll(List.of(string));
    }

    void deleteAll(Iterable<String> ids);

    default void delete(String id) {
        deleteAll(List.of(id));
    }

    default void unregisterAll(Iterable<LuceneIndexedString> objects) {
        List<String> ids = StreamSupport.stream(objects.spliterator(), false)
            .map(LuceneIndexedString::getId)
            .collect(Collectors.toList());
        deleteAll(ids);
    }

    default void unregister(LuceneIndexedString object) {
        delete(object.getId());
    }

    Iterable<Pair<LuceneIndexedString, Float>> search(Query query, int count);

    Iterable<LuceneIndexedString> findAll(int count);
}

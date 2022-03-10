package ru.rassafel.foodsharing.analyzer.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.rassafel.foodsharing.analyzer.model.LuceneObject;
import ru.rassafel.foodsharing.analyzer.repository.LuceneRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Repository
public class LuceneRepositoryImpl implements LuceneRepository {
    public static final String FIELD_BODY = "body";
    public static final String FIELD_ID = "id";
    private final SearcherManager searcherManager;
    private final IndexWriter writer;
    @Value("${lucene.numberPerPage}")
    private final int numberPerPage;

    @Override
    public LuceneObject create(String text) {
        String hash = DigestUtils.md5Hex(text).toUpperCase();
        LuceneObject object = new LuceneObject(hash, text);
        create(object);
        return object;
    }

    private void create(LuceneObject... objects) {
        List<Document> docs = Arrays.stream(objects).map(this::map)
            .collect(Collectors.toUnmodifiableList());
        try {
            writer.addDocuments(docs);
        } catch (IOException e) {
            log.error("Lucene index writer IO exception.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        Term term = new Term(FIELD_ID, id);
        try {
            writer.deleteDocuments(term);
        } catch (IOException e) {
            log.error("Lucene index writer IO exception.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LuceneObject> search(Query query, int number) {
        try {
            searcherManager.maybeRefresh();
            IndexSearcher searcher = searcherManager.acquire();
            TopDocs result = searcher.search(query, number);
            return map(searcher, result);
        } catch (IOException e) {
            log.error("Lucene searcher IO exception.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LuceneObject> search(Query query) {
        return search(query, numberPerPage);
    }

    @Override
    public List<LuceneObject> findAll() {
        MatchAllDocsQuery query = new MatchAllDocsQuery();
        return search(query);
    }

    @Override
    public List<LuceneObject> findAll(int count) {
        MatchAllDocsQuery query = new MatchAllDocsQuery();
        return search(query, count);
    }

    List<LuceneObject> map(IndexSearcher searcher, TopDocs topDocs) throws IOException {
        List<LuceneObject> result = new ArrayList<>(topDocs.scoreDocs.length);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            LuceneObject object = map(doc);
            result.add(object);
        }
        return result;
    }

    LuceneObject map(Document doc) {
        String id = doc.getField(FIELD_ID).stringValue();
        String body = doc.getField(FIELD_BODY).stringValue();
        return new LuceneObject(id, body);
    }

    Document map(LuceneObject object) {
        Document doc = new Document();
        doc.add(new StringField(FIELD_ID, object.getId(), Field.Store.YES));
        doc.add(new TextField(FIELD_BODY, object.getBody(), Field.Store.YES));
        return doc;
    }
}

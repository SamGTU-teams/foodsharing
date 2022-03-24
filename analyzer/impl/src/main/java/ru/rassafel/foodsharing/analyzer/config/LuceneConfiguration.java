package ru.rassafel.foodsharing.analyzer.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.ControlledRealTimeReopenThread;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author rassafel
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(LuceneProperties.class)
public class LuceneConfiguration {
    @Bean
    Analyzer luceneAnalyzer() {
        return new StandardAnalyzer();
    }

    @Bean
    Directory luceneDirectory(LuceneProperties properties) throws IOException {
        Path path = Paths.get(properties.getPath());
        File file = path.toFile();
        file.mkdirs();
        FSDirectory dir = FSDirectory.open(path);
        return dir;
    }

    @Bean
    IndexWriter luceneIndexWriter(Directory luceneDirectory, Analyzer luceneAnalyzer) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(luceneAnalyzer);
        IndexWriter writer = new IndexWriter(luceneDirectory, config);
        return writer;
    }

    @Bean
    SearcherManager luceneSearcherManager(IndexWriter luceneIndexWriter) throws IOException {
        SearcherManager searcherManager = new SearcherManager(luceneIndexWriter, false, false, null);
        ControlledRealTimeReopenThread<IndexSearcher> cRTRThread = new ControlledRealTimeReopenThread<>(
            luceneIndexWriter, searcherManager, 5, 0.025);
        cRTRThread.setDaemon(true);
        cRTRThread.setName("IndexReaderUpdaterThread");
        cRTRThread.start();
        return searcherManager;
    }
}
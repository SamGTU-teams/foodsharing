package ru.rassafel.foodsharing.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.search.FuzzyQuery;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@ConfigurationProperties(prefix = LuceneProperties.PREFIX)
@Getter
@Setter
public class LuceneProperties {
    public static final String PREFIX = "lucene";

    private String path = "/lucene/indexDir";
    private int luceneMaxResults = 100;
    private Integer maxEdits = FuzzyQuery.defaultMaxEdits;
    private int prefixLength = FuzzyQuery.defaultPrefixLength;
    private int maxExpansions = FuzzyQuery.defaultMaxExpansions;
    private boolean transpositions = FuzzyQuery.defaultTranspositions;
}

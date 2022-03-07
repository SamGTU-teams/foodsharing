package ru.rassafel.foodsharing.analyzer.config;

import lombok.Getter;
import lombok.Setter;
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

    private int numberPerPage = 100;
}

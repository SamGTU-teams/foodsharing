package ru.rassafel.foodsharing.analyzer.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author rassafel
 */
@Configuration
@ConditionalOnProperty(name = {"feign.analyzer.url"})
public class AnalyzerAutoConfiguration {
}

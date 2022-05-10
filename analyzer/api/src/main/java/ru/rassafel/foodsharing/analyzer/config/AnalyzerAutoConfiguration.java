package ru.rassafel.foodsharing.analyzer.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author rassafel
 */
@Configuration
@ConditionalOnProperty(name = {"feign.analyzer.url"})
@EnableFeignClients
public class AnalyzerAutoConfiguration {
}

package ru.rassafel.foodsharing.analyzer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rassafel.foodsharing.analyzer.model.mapper.ScoreProductMapper;

/**
 * @author rassafel
 */
@Configuration
public class AnalyzerConfiguration {
    @Bean
    ScoreProductMapper scoreProductMapper() {
        return ScoreProductMapper.INSTANCE;
    }
}

package ru.rassafel.foodsharing.analyzer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author rassafel
 */
@Configuration
@ComponentScan("ru.rassafel.foodsharing.analyzer.controller")
public class AnalyzerAutoConfiguration {
}

package ru.rassafel.foodsharing.analyzer.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.rassafel.foodsharing.analyzer.controller.ProductAnalyzerController;
import ru.rassafel.foodsharing.analyzer.controller.stub.ProductAnalyzerControllerStub;

/**
 * @author rassafel
 */
@Configuration
@ComponentScan("ru.rassafel.foodsharing.analyzer.controller")
public class AnalyzerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ProductAnalyzerController.class)
    public ProductAnalyzerControllerStub productAnalyzerControllerStub(){
        return new ProductAnalyzerControllerStub();
    }
}

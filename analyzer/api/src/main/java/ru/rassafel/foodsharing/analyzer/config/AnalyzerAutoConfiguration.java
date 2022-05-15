package ru.rassafel.foodsharing.analyzer.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.rassafel.foodsharing.analyzer.controller.stub.ProductAnalyzerControllerStub;

/**
 * @author rassafel
 */
@Configuration
@ConditionalOnProperty(name = {"feign.analyzer.url"})
@EnableFeignClients
public class AnalyzerAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "product-analyzer.stub-mode", havingValue = "true")
    @Primary
    public ProductAnalyzerControllerStub productAnalyzerControllerStub(){
        return new ProductAnalyzerControllerStub();
    }

}

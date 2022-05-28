package ru.rassafel.foodsharing.analyzer.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.rassafel.foodsharing.analyzer.controller.feign.ProductAnalyzerControllerFeign;

/**
 * @author rassafel
 */
@Configuration
@EnableFeignClients(clients = ProductAnalyzerControllerFeign.class)
@ComponentScan("ru.rassafel.foodsharing.analyzer.controller")
public class AnalyzerAutoConfiguration {
}

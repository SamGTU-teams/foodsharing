package ru.rassafel.foodsharing.analyzer.controller.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import ru.rassafel.foodsharing.analyzer.controller.ProductAnalyzerController;

/**
 * @author rassafel
 */
@FeignClient(name = "AnalyzerService", url = "${feign.analyzer.url}")
@ConditionalOnProperty(name = "product-analyzer.stub-mode", havingValue = "false")
public interface ProductAnalyzerControllerFeign extends ProductAnalyzerController {
}

package ru.rassafel.foodsharing.analyzer.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.rassafel.foodsharing.analyzer.controller.ProductsAnalyzerController;

/**
 * @author rassafel
 */
@FeignClient(name = "AnalyzerService", url = "${feign.analyzer.url}")
public interface ProductsAnalyzerControllerFeign extends ProductsAnalyzerController {
}

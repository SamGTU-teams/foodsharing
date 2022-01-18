package ru.rassafel.foodsharing.analyzer.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.rassafel.foodsharing.analyzer.controller.AnalyzerController;

/**
 * @author rassafel
 */
@FeignClient(name = "AnalyzerService", url = "${feign.analyzer.url}")
public interface AnalyzerControllerFeign extends AnalyzerController {
}

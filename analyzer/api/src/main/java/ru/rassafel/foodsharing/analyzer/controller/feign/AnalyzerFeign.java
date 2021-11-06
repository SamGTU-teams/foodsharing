package ru.rassafel.foodsharing.analyzer.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author rassafel
 */
@FeignClient(name = "AnalyzerService", url = "${feign.analyzer.url}")
public interface AnalyzerFeign {
}

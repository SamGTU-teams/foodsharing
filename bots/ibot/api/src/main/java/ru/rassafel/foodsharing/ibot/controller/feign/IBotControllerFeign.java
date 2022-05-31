package ru.rassafel.foodsharing.ibot.controller.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import ru.rassafel.foodsharing.ibot.controller.IBotController;

/**
 * @author rassafel
 */
@FeignClient(name = "IBotService", url = "${feign.ibot.url}")
@ConditionalOnProperty(prefix = "feign.ibot", name = "url")
public interface IBotControllerFeign extends IBotController {
}

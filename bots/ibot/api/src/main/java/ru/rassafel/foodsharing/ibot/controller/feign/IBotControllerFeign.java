package ru.rassafel.foodsharing.ibot.controller.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import ru.rassafel.foodsharing.ibot.controller.IBotController;

/**
 * @author rassafel
 */
@FeignClient(name = "ibot", url = "${feign.ibot.url}")
@ConditionalOnProperty(name = "ibot.stub-mode", havingValue = "false")
public interface IBotControllerFeign extends IBotController {
}

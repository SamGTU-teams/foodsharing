package ru.rassafel.foodsharing.ibot.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.rassafel.foodsharing.ibot.controller.IBotController;

/**
 * @author rassafel
 */
@FeignClient(name = "ibot", url = "${feign.ibot.url}")
public interface IBotControllerFeign extends IBotController {
}

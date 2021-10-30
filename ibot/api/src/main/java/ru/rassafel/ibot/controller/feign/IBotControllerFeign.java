package ru.rassafel.ibot.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.rassafel.ibot.controller.IBotController;

/**
 * @author rassafel
 */
@FeignClient(name = "IBotService", url = "${feign.ibot.url}")
public interface IBotControllerFeign extends IBotController {
}

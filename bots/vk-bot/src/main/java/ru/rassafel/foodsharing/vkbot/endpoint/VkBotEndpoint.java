package ru.rassafel.foodsharing.vkbot.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rassafel.foodsharing.vkbot.model.VkUpdate;
import ru.rassafel.foodsharing.vkbot.service.VkBotHandlerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class VkBotEndpoint {

    private final VkBotHandlerService vkBotHandlerService;

    @PostMapping
    public ResponseEntity<String> handle(@RequestBody VkUpdate update) {
        log.debug("Got request from VK : {}", update);
        return ResponseEntity.ok(vkBotHandlerService.handleUpdate(update));
    }

//    @PostMapping
//    public ResponseEntity<String> handle(@RequestBody String update) {
//        log.debug("Got request from VK : {}", update);
////        return ResponseEntity.ok(vkBotHandlerService.handleUpdate(update));
//        return ResponseEntity.ok("2622bee3");
//    }

}

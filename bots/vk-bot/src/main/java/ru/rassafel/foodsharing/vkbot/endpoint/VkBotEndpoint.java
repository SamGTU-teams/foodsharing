package ru.rassafel.foodsharing.vkbot.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rassafel.foodsharing.vkbot.model.dto.VkUpdate;
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
        ResponseEntity<String> response = ResponseEntity.ok(vkBotHandlerService.handleUpdate(update));
        log.debug("Sending response to Vk : {}", response);
        return response;
    }

}

package ru.rassafel.foodsharing.vkbot.endpoint;

import com.vk.api.sdk.objects.callback.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rassafel.foodsharing.session.repository.CallbackLockRepository;
import ru.rassafel.foodsharing.vkbot.model.dto.VkUpdate;
import ru.rassafel.foodsharing.vkbot.service.VkBotHandlerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/callback")
@Slf4j
public class VkBotEndpoint {
    public static final String OK_MESSAGE = "ok";
    private final CallbackLockRepository lockRepository;
    private final VkBotHandlerService vkBotHandlerService;
    private final RabbitTemplate template;

    @PostMapping
    public ResponseEntity<String> handle(@RequestBody VkUpdate update) {
        log.debug("Got request from VK : {}", update);
        if (Type.CONFIRMATION.equals(update.getType())) {
            return ResponseEntity.ok(vkBotHandlerService.handleUpdate(update));
        }

        Long chatId = (long) update.getObject().getMessage().getFromId();
        Long messageId = (long) update.getObject().getMessage().getId();
        if (!lockRepository.lock(chatId, messageId)) {
            log.debug("Skip message from chat with id = {}", chatId);
            return ResponseEntity.ok(OK_MESSAGE);
        }

        template.convertAndSend(update);
        return ResponseEntity.ok(OK_MESSAGE);
    }
}

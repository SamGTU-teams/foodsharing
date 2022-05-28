package ru.rassafel.foodsharing.vkbot.controller.impl;

import com.vk.api.sdk.objects.callback.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rassafel.foodsharing.session.repository.CallbackLockRepository;
import ru.rassafel.foodsharing.vkbot.controller.CallbackController;
import ru.rassafel.foodsharing.vkbot.model.vk.VkUpdate;
import ru.rassafel.foodsharing.vkbot.service.VkBotHandlerService;

/**
 * @author rassafel
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class CallbackControllerImpl implements CallbackController {
    private final CallbackLockRepository lockRepository;
    private final VkBotHandlerService vkBotHandlerService;
    private final RabbitTemplate template;

    @Override
    public ResponseEntity<String> acceptUpdate(VkUpdate update) {
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

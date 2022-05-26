package ru.rassafel.foodsharing.vkparser.controller.impl;

import com.vk.api.sdk.events.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rassafel.foodsharing.vkparser.controller.CallbackController;
import ru.rassafel.foodsharing.vkparser.model.vk.CallbackMessage;
import ru.rassafel.foodsharing.vkparser.repository.GroupCallbackRepository;
import ru.rassafel.foodsharing.vkparser.service.CallbackService;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class CallbackControllerImpl implements CallbackController {
    private final CallbackService service;
    private final RabbitTemplate callbackRabbitTemplate;
    private final GroupCallbackRepository callbackRepository;

    @Override
    public ResponseEntity<String> acceptUpdate(CallbackMessage message) {
        Integer groupId = message.getGroupId();
        Integer postId = message.getWallpost().getId();
        log.debug("Accepted post ({}) from group ({}) with type = {}", postId, groupId, message.getType().name());
        if (Events.CONFIRMATION.equals(message.getType())) {
            String confirmation = service.confirmation(groupId);
            return ResponseEntity.ok(confirmation);
        }
        if (!callbackRepository.registerPost(groupId, postId)) {
            log.debug("Skip post ({}) from group with id = {}", postId, groupId);
            return ResponseEntity.ok(OK_MESSAGE);
        }
        callbackRabbitTemplate.convertAndSend(message);
        return ResponseEntity.ok(OK_MESSAGE);
    }
}

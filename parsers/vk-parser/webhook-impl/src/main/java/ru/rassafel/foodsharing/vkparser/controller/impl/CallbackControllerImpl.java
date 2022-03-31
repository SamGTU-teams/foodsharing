package ru.rassafel.foodsharing.vkparser.controller.impl;

import com.vk.api.sdk.events.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rassafel.foodsharing.vkparser.controller.CallbackController;
import ru.rassafel.foodsharing.vkparser.model.vk.CallbackMessage;
import ru.rassafel.foodsharing.vkparser.service.CallbackService;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class CallbackControllerImpl implements CallbackController {
    private final CallbackService service;

    @Override
    public ResponseEntity<?> acceptUpdate(CallbackMessage message) {
        log.debug("Accepted message with type: {}", message.getType().name());
        if (Events.CONFIRMATION.equals(message.getType())) {
            String conformation = service.confirmation(message.getGroupId());
            return ResponseEntity.ok(conformation);
        }
        if (Events.WALL_POST_NEW.equals(message.getType())) {
            try {
                service.wallpostNew(message.getGroupId(), message.getWallpost(), message.getSecret());
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
        }
        return ResponseEntity.ok("ok");
    }
}

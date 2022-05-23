package ru.rassafel.foodsharing.ad.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rassafel.foodsharing.ad.controller.AdController;
import ru.rassafel.foodsharing.ad.model.dto.AdPostDto;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class AdControllerImpl implements AdController {
    private final RabbitTemplate template;

    @Override
    public ResponseEntity<?> createAd(AdPostDto message) {
        template.convertAndSend(message);
        return ResponseEntity.ok().build();
    }
}

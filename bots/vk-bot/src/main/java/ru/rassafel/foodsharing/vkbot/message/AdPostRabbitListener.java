package ru.rassafel.foodsharing.vkbot.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.ad.model.dto.AdPostDto;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.dto.To;
import ru.rassafel.foodsharing.session.service.Messenger;
import ru.rassafel.foodsharing.vkbot.repository.VkUserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(queues = {"${spring.rabbitmq.ad-post.queue}"})
public class AdPostRabbitListener {
    private static final int PAGE_SIZE = 100;
    private final Validator validator;
    private final VkUserRepository vkUserRepository;
    private final Messenger messenger;

    @RabbitHandler
    public void receiveMessage(AdPostDto adPostDto) {
        Set<ConstraintViolation<AdPostDto>> violations = validator.validate(adPostDto);
        if (!violations.isEmpty()) {
            log.debug("Validation exception", new ConstraintViolationException(violations));
            return;
        }
        int pageNum = 0;
        Page<Long> userIds;
        while ((userIds = vkUserRepository.findAllUserIds(PageRequest.of(pageNum++, PAGE_SIZE))).hasContent()) {
            SessionResponse response = SessionResponse.builder()
                .sendTo(new To(userIds.getContent()))
                .message(adPostDto.getMessage())
                .build();
            messenger.send(response);
        }
    }
}

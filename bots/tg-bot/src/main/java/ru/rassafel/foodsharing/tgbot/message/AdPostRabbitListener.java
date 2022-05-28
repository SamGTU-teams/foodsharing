package ru.rassafel.foodsharing.tgbot.message;

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
import ru.rassafel.foodsharing.tgbot.repository.TgUserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(queues = {"${spring.rabbitmq.ad-post.queue}"})
public class AdPostRabbitListener {
    private final Validator validator;
    private final BlockingQueue<SessionResponse> queue;
    private final TgUserRepository tgUserRepository;
    private static final int PAGE_SIZE = 50;

    @RabbitHandler
    public void receiveMessage(AdPostDto adPostDto) {
        Set<ConstraintViolation<AdPostDto>> violations = validator.validate(adPostDto);
        if (!violations.isEmpty()) {
            log.debug("Validation exception", new ConstraintViolationException(violations));
            return;
        }
        int pageNum = 0;
        Page<Long> userIds;
        while ((userIds = tgUserRepository.findAllUserIds(PageRequest.of(pageNum++, PAGE_SIZE))).hasContent()) {
            userIds.stream().map(id ->
                    SessionResponse.builder()
                        .sendTo(new To(id))
                        .message(adPostDto.getMessage())
                        .build())
                .forEach(sr -> {
                    try {
                        queue.put(sr);
                    } catch (InterruptedException e) {
                        log.error("Caught an exception while put ad message to queue");
                    }
                });
        }
    }
}

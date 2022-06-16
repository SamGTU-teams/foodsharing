package ru.rassafel.foodsharing.vkbot.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.session.repository.CallbackLockRepository;
import ru.rassafel.foodsharing.vkbot.model.vk.VkUpdate;
import ru.rassafel.foodsharing.vkbot.service.VkBotHandler;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(queues = {"${spring.rabbitmq.vk-bot-callback.queue}"})
public class CallbackRabbitListener {
    private final CallbackLockRepository lockRepository;
    private final VkBotHandler vkBotHandlerService;

    @RabbitHandler
    public void receiveMessage(VkUpdate update) {
        Long chatId = (long) update.getObject().getMessage().getFromId();
        log.debug("Receive message from chat with id = {}", chatId);
        vkBotHandlerService.handle(update);
        log.debug("Release lock for chat with id = {}", chatId);
        lockRepository.unlock(chatId);
    }
}

package ru.rassafel.foodsharing.tgbot.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rassafel.foodsharing.session.repository.CallbackLockRepository;
import ru.rassafel.foodsharing.tgbot.service.TgBotHandler;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Slf4j
@Component
@RabbitListener(queues = {"${spring.rabbitmq.tg-bot-callback.queue}"})
public class CallbackRabbitListener {
    private final CallbackLockRepository lockRepository;
    private final TgBotHandler handler;

    @RabbitHandler
    public void receiveMessage(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            log.debug("Receive message from chat with id = {}", chatId);
            handler.handle(update);
            log.debug("Release lock for chat with id = {}", chatId);
            lockRepository.unlock(chatId);
        }
//        ToDo: add handler for other types
    }
}

package ru.rassafel.foodsharing.tgbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rassafel.foodsharing.session.service.Messenger;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class TgMessenger implements Messenger {
    private final TgBotHandlerService service;

    @Override
    public void send(String message, Integer... userId) {
        Arrays.stream(userId).forEach(id -> {
            SendMessage sendMessage = new SendMessage(id.longValue(), message);
            try {
                service.execute(sendMessage);
                log.debug("Successfully sent message {userId={}, message={}}", userId, message);
            } catch (TelegramApiException e) {
                log.error("Caught an exception while sending message to user with id = {} and text = {}, errorMessage = {}",
                    id, message, e.getMessage());
            }
        });
    }
}

package ru.rassafel.foodsharing.tgbot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.service.Messenger;
import ru.rassafel.foodsharing.tgbot.model.mapper.TgBotDtoMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class TgMessenger implements Messenger {
    private final AbsSender sender;
    private final TgBotDtoMapper mapper;

    @Override
    public void send(SessionResponse response) {
        SendMessage sendMessage = mapper.mapToSendMessage(response);
        sendMessage.enableWebPagePreview();
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Caught exception while sending message : {}", e.getMessage());
        }
    }
}

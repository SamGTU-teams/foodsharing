package ru.rassafel.foodsharing.tgbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.service.Messenger;
import ru.rassafel.foodsharing.tgbot.model.mapper.TgBotDtoMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TgMessenger implements Messenger {

    private final TgBotHandlerService service;
    private final TgBotDtoMapper mapper;

    @Override
    public void sendBatch(List<SessionResponse> responses) {
        for (SessionResponse response : responses) {
            SendMessage sendMessage = mapper.map(response);
            try {
                service.execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Caught exception while sending message : {}", e.getMessage());
            }
        }
    }
}

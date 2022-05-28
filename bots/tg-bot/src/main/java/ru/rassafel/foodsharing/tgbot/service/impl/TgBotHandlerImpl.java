package ru.rassafel.foodsharing.tgbot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rassafel.foodsharing.session.exception.BotException;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.dto.To;
import ru.rassafel.foodsharing.session.service.Messenger;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.session.SessionService;
import ru.rassafel.foodsharing.session.templates.MainTemplates;
import ru.rassafel.foodsharing.tgbot.model.mapper.TgBotDtoMapper;
import ru.rassafel.foodsharing.tgbot.service.TgBotHandler;

@Component
@RequiredArgsConstructor
@Slf4j
public class TgBotHandlerImpl implements TgBotHandler {
    private final Messenger messenger;
    private final TemplateEngine templateEngine;
    private final TgBotDtoMapper mapper;
    private final SessionService service;

    @Override
    public Void handle(Update update) {
        SessionRequest request = mapper.mapFromUpdate(update);
        SessionResponse sessionResponse;
        try {
            sessionResponse = service.handle(request);
        } catch (BotException ex) {
            sessionResponse = SessionResponse.builder()
                .sendTo(new To(ex.getSendTo()))
                .message(ex.getMessage())
                .build();
        } catch (Exception ex) {
            log.error("Caught an exception with message {}", ex.getMessage());
            sessionResponse = SessionResponse.builder()
                .sendTo(new To(update.getMessage().getChatId()))
                .message(templateEngine.compileTemplate(MainTemplates.ERROR_ON_SERVER))
                .build();
        }
        messenger.send(sessionResponse);
        return null;
    }
}

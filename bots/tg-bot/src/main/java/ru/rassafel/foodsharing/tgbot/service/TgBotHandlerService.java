package ru.rassafel.foodsharing.tgbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rassafel.foodsharing.session.exception.BotException;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.dto.To;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.session.SessionService;
import ru.rassafel.foodsharing.session.templates.MainTemplates;
import ru.rassafel.foodsharing.tgbot.model.mapper.TgBotDtoMapper;

import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
@Slf4j
public class TgBotHandlerService extends TelegramWebhookBot {
    private final String botUsername;
    private final String botToken;
    private final String webHookPath;
    private final BlockingQueue<SessionResponse> queue;
    private final TemplateEngine templateEngine;
    private final TgBotDtoMapper mapper;
    private final SessionService service;

    @Override
    public SendMessage onWebhookUpdateReceived(Update update) {
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
        try {
            queue.put(sessionResponse);
        } catch (InterruptedException e) {
            log.error("Caught an exception while put to queue : {}", e.getMessage());
        }
        return mapper.map(sessionResponse);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }
}

package ru.rassafel.foodsharing.tgbot.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.service.SessionService;
import ru.rassafel.foodsharing.tgbot.mapper.TgBotDtoMapper;

@RequiredArgsConstructor
@Slf4j
public class TgBotHandlerService extends TelegramWebhookBot {

    private final String botUsername;
    private final String botToken;
    private final String webHookPath;
    @Autowired
    private SessionService service;
    @Autowired
    private TgBotDtoMapper mapper;

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        SessionRequest request = mapper.mapFromUpdate(update);
        SessionResponse sessionResponse;
        try {
            sessionResponse = service.handle(request);
        } catch (BotException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            SendMessage ifException = new SendMessage();
            ifException.setChatId(update.getMessage().getChatId());
            ifException.setText("Some exception");
            return ifException;
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

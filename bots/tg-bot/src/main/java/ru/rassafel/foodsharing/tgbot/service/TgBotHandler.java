package ru.rassafel.foodsharing.tgbot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rassafel.foodsharing.session.service.UpdateHandler;

/**
 * @author rassafel
 */
public interface TgBotHandler extends UpdateHandler<Update, Void> {
}

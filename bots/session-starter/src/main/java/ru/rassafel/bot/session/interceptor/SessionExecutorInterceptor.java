package ru.rassafel.bot.session.interceptor;

import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.user.User;
import ru.rassafel.bot.session.type.BotSession;

public interface SessionExecutorInterceptor {
    SessionResponse handle(SessionRequest request, User user, BotSession next);
}

package ru.rassafel.foodsharing.session.interceptor;

import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.service.session.type.BotSession;

public interface SessionInterceptor {
    SessionResponse handle(SessionRequest request, User user, BotSession next);
}

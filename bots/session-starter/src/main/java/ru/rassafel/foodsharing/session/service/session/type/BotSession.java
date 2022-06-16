package ru.rassafel.foodsharing.session.service.session.type;

import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.User;

public interface BotSession {
    SessionResponse execute(SessionRequest request, User user);

    String getName();
}

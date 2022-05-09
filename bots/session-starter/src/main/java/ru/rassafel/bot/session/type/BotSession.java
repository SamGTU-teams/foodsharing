package ru.rassafel.bot.session.type;

import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.User;

public interface BotSession {

    SessionResponse execute(SessionRequest request, User user);

    String getName();
}

package ru.rassafel.bot.session.type;

import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.user.User;

public interface BotSession {

    SessionResponse execute(SessionRequest request, User user);

    default void setStep(int step) {
    }

    String getName();
}

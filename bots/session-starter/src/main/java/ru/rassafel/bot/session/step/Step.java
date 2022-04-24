package ru.rassafel.bot.session.step;

import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.user.User;

public interface Step {

    void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user);

}

package ru.rassafel.bot.session.step;

import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.User;

public interface Step {

    void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user);

}

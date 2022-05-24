package ru.rassafel.foodsharing.session.step;

import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.User;

public interface Step {
    void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user);
}

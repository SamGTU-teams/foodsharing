package ru.rassafel.foodsharing.session.service;

import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;

/**
 * @author rassafel
 */
public interface SessionService {
    SessionResponse handle(SessionRequest request);
}

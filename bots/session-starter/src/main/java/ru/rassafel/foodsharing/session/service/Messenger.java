package ru.rassafel.foodsharing.session.service;

import ru.rassafel.foodsharing.session.model.dto.SessionResponse;

import java.util.List;

public interface Messenger {
    default void send(List<SessionResponse> responses) {
        responses.forEach(this::send);
    }

    void send(SessionResponse response);
}

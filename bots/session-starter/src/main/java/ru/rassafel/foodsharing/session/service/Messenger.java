package ru.rassafel.foodsharing.session.service;

import ru.rassafel.foodsharing.session.model.dto.SessionResponse;

import java.util.List;

public interface Messenger {
    void sendBatch(List<SessionResponse> responses);
}

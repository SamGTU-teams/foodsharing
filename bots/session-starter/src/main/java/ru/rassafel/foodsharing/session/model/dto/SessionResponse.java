package ru.rassafel.foodsharing.session.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class SessionResponse {
    private String message;
    private BotButtons buttons;
    private To sendTo;
}

package ru.rassafel.bot.session.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SessionResponse {
    private String message;
    private BotButtons buttons;
    private To sendTo;
}

package ru.rassafel.bot.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rassafel.bot.session.model.BotButtons;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SessionResponse {

    private String message;
    private BotButtons buttons;
    private To sendTo;

}

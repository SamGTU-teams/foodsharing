package ru.rassafel.bot.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SessionResponse {

    private String message;
    private List<String> buttons;
    private To sendTo;

}

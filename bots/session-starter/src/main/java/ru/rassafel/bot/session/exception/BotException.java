package ru.rassafel.bot.session.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BotException extends RuntimeException {

    private Long sendTo;
    private String message;

}

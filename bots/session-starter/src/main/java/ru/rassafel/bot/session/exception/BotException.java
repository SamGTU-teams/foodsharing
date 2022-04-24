package ru.rassafel.bot.session.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BotException extends RuntimeException {
    private Long sendTo;

    public BotException(Long sendTo, String message) {
        super(message);
        this.sendTo = sendTo;
    }
}

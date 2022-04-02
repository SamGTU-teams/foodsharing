package ru.rassafel.bot.session.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ExpiredLocationObjectException extends BotException {

    public ExpiredLocationObjectException(Long sendTo, String message) {
        super(sendTo, message);
    }
}

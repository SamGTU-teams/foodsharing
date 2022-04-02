package ru.rassafel.bot.session.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SessionNotFoundException extends BotException {

    private final Long sendToId;
    private final String message;

}

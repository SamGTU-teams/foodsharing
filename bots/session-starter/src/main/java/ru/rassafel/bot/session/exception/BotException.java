package ru.rassafel.bot.session.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.rassafel.foodsharing.common.exception.ApiException;

@NoArgsConstructor
@Getter
public class BotException extends ApiException {
    private Long sendTo;

    public BotException(Long sendTo, String message) {
        super(message);
        this.sendTo = sendTo;
    }
}

package ru.rassafel.bot.session.exception;

public class UnrecognizedCommandException extends SessionNotFoundException {


    public UnrecognizedCommandException(Long sendToChatId, String message) {
        super(sendToChatId, message);
    }
}

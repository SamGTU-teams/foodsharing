package ru.rassafel.bot.session.service;

public interface Messenger {

    void send(String message, Integer...userId);

}

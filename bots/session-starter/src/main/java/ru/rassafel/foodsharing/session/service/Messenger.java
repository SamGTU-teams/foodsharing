package ru.rassafel.foodsharing.session.service;

public interface Messenger {
    void send(String message, Integer... userId);
}

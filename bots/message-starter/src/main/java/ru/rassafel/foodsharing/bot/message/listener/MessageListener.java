package ru.rassafel.foodsharing.bot.message.listener;

/**
 * @author rassafel
 */
public interface MessageListener<T> {
    void receiveMessage(T message);
}

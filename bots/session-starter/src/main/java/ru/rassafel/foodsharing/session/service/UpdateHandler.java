package ru.rassafel.foodsharing.session.service;

/**
 * @author rassafel
 */
public interface UpdateHandler<U, R> {
    R handle(U update);
}

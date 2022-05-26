package ru.rassafel.foodsharing.session.repository;

/**
 * @author rassafel
 */
public interface CallbackLockRepository {
    boolean lock(Long userId, Long messageId);

    void unlock(Long userId);
}

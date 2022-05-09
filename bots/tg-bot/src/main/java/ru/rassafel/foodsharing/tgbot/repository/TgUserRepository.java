package ru.rassafel.foodsharing.tgbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rassafel.foodsharing.tgbot.model.TgUser;

public interface TgUserRepository extends JpaRepository<TgUser, Long> {
}

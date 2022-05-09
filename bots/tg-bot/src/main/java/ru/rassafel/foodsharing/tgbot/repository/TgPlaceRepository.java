package ru.rassafel.foodsharing.tgbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rassafel.foodsharing.tgbot.model.TgUserPlace;

import java.util.Collection;

public interface TgPlaceRepository extends JpaRepository<TgUserPlace, Long> {
    Collection<TgUserPlace> findByUserId(Long userId);
}

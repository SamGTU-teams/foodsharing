package ru.rassafel.bot.session.repository.place;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rassafel.bot.session.model.entity.place.TgUserPlace;

import java.util.Collection;

public interface TgUserPlaceRepository extends JpaRepository<TgUserPlace, Long> {
    Collection<TgUserPlace> findByUserId(Long userId);
}

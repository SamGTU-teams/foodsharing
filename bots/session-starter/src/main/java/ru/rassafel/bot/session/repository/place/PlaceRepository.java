package ru.rassafel.bot.session.repository.place;

import ru.rassafel.bot.session.model.entity.place.Place;

import java.util.Collection;

public interface PlaceRepository<T extends Place> {

    Collection<? extends Place> findByUserId(Long userId);

    void savePlaces(Collection<T> place);
}

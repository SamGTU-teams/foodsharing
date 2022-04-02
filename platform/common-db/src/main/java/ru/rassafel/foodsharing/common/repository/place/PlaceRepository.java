package ru.rassafel.foodsharing.common.repository.place;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.rassafel.foodsharing.common.model.entity.geo.Place;

import java.util.Collection;

public interface PlaceRepository<T extends Place> {

    Collection<? extends Place> findByUserId(Long userId);

    void savePlaces(Collection<T> place);
}

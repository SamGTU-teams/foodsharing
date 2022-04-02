package ru.rassafel.foodsharing.common.repository.place;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rassafel.foodsharing.common.model.entity.geo.TgUserPlace;

import java.util.Collection;

public interface  TgUserPlaceRepository extends JpaRepository<TgUserPlace, Long> {

    Collection<TgUserPlace> findByUserId(Long userId);

}

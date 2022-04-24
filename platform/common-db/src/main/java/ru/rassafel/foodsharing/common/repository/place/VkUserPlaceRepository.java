package ru.rassafel.foodsharing.common.repository.place;

import org.springframework.data.repository.CrudRepository;
import ru.rassafel.foodsharing.common.model.entity.geo.VkUserPlace;

import java.util.Collection;

public interface VkUserPlaceRepository extends CrudRepository<VkUserPlace, Long> {

    Collection<VkUserPlace> findByUserId(Long userId);

}

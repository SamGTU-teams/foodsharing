package ru.rassafel.foodsharing.vkbot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUserPlace;

import java.util.Collection;

public interface VkUserPlaceRepository extends CrudRepository<VkUserPlace, Long> {

    Collection<VkUserPlace> findByUserId(Long userId);

}

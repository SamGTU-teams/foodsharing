package ru.rassafel.bot.session.repository.place;

import org.springframework.data.repository.CrudRepository;
import ru.rassafel.bot.session.model.entity.place.VkUserPlace;

import java.util.Collection;

public interface VkUserPlaceRepository extends CrudRepository<VkUserPlace, Long> {

    Collection<VkUserPlace> findByUserId(Long userId);

}

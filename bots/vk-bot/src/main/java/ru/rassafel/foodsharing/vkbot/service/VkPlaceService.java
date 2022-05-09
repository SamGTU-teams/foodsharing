package ru.rassafel.foodsharing.vkbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.model.dto.LocationDto;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.foodsharing.common.model.PlatformType;
import ru.rassafel.foodsharing.common.model.entity.geo.GeoPointEmbeddable;
import ru.rassafel.foodsharing.vkbot.model.domain.VkUserPlace;
import ru.rassafel.foodsharing.vkbot.repository.VkUserPlaceRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class VkPlaceService implements PlaceService {

    private final VkUserPlaceRepository repository;

    @Override
    public Collection<Place> findByUserId(Long id) {
        return new ArrayList<>(repository.findByUserId(id));
    }

    @Override
    public void save(Place place) {
        if(place instanceof VkUserPlace){
            repository.save((VkUserPlace) place);
        }else {
            throw new IllegalArgumentException("Provided place is not from VK!");
        }
    }

    @Override
    public void deletePlace(Place place) {
        if(place instanceof VkUserPlace){
            repository.delete((VkUserPlace) place);
        }else {
            throw new IllegalArgumentException("Provided place is not from VK!");
        }
    }

    @Override
    public Place createPlace(User linkWith, LocationDto location) {
        return new VkUserPlace()
            .withUser(linkWith)
            .withGeo(new GeoPointEmbeddable(location.getLatitude(), location.getLongitude()));
    }
}

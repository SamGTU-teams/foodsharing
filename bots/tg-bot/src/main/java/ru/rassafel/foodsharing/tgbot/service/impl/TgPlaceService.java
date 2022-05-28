package ru.rassafel.foodsharing.tgbot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.common.model.mapper.GeoPointEmbeddableMapper;
import ru.rassafel.foodsharing.session.model.entity.Place;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.service.PlaceService;
import ru.rassafel.foodsharing.tgbot.model.domain.TgUserPlace;
import ru.rassafel.foodsharing.tgbot.repository.TgPlaceRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TgPlaceService implements PlaceService {
    private final TgPlaceRepository repository;
    private final GeoPointEmbeddableMapper mapper;

    @Override
    public Collection<TgUserPlace> findByUserId(Long id) {
        return new ArrayList<>(repository.findByUserId(id));
    }

    @Override
    public void save(Place place) {
        if (place instanceof TgUserPlace) {
            repository.save((TgUserPlace) place);
        } else {
            throw new IllegalArgumentException("Place is not TG place!");
        }
    }

    @Override
    public void deletePlace(Place place) {
        if (place instanceof TgUserPlace) {
            repository.delete((TgUserPlace) place);
        } else {
            throw new IllegalArgumentException("Place is not TG place!");
        }
    }

    @Override
    public TgUserPlace createPlace(User linkWith, GeoPoint geoPoint) {
        TgUserPlace place = new TgUserPlace();
        place.withUser(linkWith)
            .withGeo(mapper.dtoToEntity(geoPoint));
        return place;
    }

    @Override
    public TgUserPlace createPlace(User linkWith, GeoPoint geoPoint, String address) {
        TgUserPlace place = createPlace(linkWith, geoPoint);
        place.setAddress(address);
        return place;
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        repository.deleteAllByUserId(userId);
    }
}

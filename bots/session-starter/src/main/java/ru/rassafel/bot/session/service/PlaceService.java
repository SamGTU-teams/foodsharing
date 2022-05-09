package ru.rassafel.bot.session.service;

import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.foodsharing.common.model.GeoPoint;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public interface PlaceService {
    Collection<Place> findByUserId(Long id);

    void save(Place place);

    void deletePlace(Place place);

    Place createPlace(User linkWith, GeoPoint geoPoint);

    default Map<Integer, String> getUsersPlacesNamesMap(User user) {
        Collection<Place> usersPlaces = findByUserId(user.getId());
        return getUsersPlacesNamesMap(usersPlaces);
    }

    default Map<Integer, String> getUsersPlacesNamesMap(Collection<Place> places) {
        AtomicInteger counter = new AtomicInteger(1);
        return places.stream()
            .map(Place::getName)
            .collect(Collectors.toMap(
                o -> counter.getAndIncrement(),
                o -> o
            ));
    }

    default String getUsersPlaceMapMessage(User user, String additionalMessage) {
        return getUsersPlacesNamesMap(user)
            .entrySet()
            .stream()
            .map(entry -> entry.getKey() + "." + entry.getValue())
            .collect(Collectors.joining("\n", "",
                "\n\n" + additionalMessage));
    }

    default String getUsersPlaceMapMessage(User user) {
        return getUsersPlaceMapMessage(user, "");
    }
}

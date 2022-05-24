package ru.rassafel.foodsharing.session.service;

import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.session.model.entity.Place;
import ru.rassafel.foodsharing.session.model.entity.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public interface PlaceService {
    Collection<Place> findByUserId(Long id);

    void save(Place place);

    void deletePlace(Place place);

    Place createPlace(User linkWith, GeoPoint geoPoint);

    Place createPlace(User linkWith, GeoPoint geoPoint, String address);

    void deleteAllByUserId(Long userId);

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

    // ToDo: remove if not in use
    default String getUsersPlaceMapMessage(User user, String additionalMessage) {
        return getUsersPlacesNamesMap(user)
            .entrySet()
            .stream()
            .map(entry -> entry.getKey() + "." + entry.getValue())
            .collect(Collectors.joining("\n", "",
                "\n\n" + additionalMessage));
    }

    // ToDo: remove if not in use
    default String getUsersPlaceMapMessage(User user) {
        return getUsersPlaceMapMessage(user, "");
    }
}

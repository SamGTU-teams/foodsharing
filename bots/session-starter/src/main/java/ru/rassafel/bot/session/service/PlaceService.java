package ru.rassafel.bot.session.service;

import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.model.dto.LocationDto;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.foodsharing.common.model.PlatformType;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public interface PlaceService {

    Collection<Place> findByUserId(Long id);

    void save(Place place);

    void deletePlace(Place place);

    Place createPlace(User linkWith, LocationDto location);

    default Map<Integer, String> getUsersPlacesNamesMap(User user) {
        Collection<Place> usersPlaces = findByUserId(user.getId());
        return getUsersPlacesNamesMap(usersPlaces);
    }

    default Map<Integer, String> getUsersPlacesNamesMap(Collection<Place> places) {
        int[] productCounter = {1};
        return places.stream().map(Place::getName).collect(Collectors.toMap(
            o -> productCounter[0]++,
            o -> o
        ));
    }

    default String getUsersPlaceMapMessage(User user, String additionalMessage) {
        return getUsersPlacesNamesMap(user).entrySet().stream().map(entry -> entry.getKey() + "." + entry.getValue())
            .collect(Collectors.joining("\n"))
            + "\n\n" + additionalMessage;
    }

    default String getUsersPlaceMapMessage(User user, PlatformType platformType) {
        return getUsersPlaceMapMessage(user, "");
    }
}

package ru.rassafel.bot.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.model.entity.place.Place;
import ru.rassafel.bot.session.model.entity.place.TgUserPlace;
import ru.rassafel.bot.session.model.entity.place.VkUserPlace;
import ru.rassafel.bot.session.model.entity.user.User;
import ru.rassafel.bot.session.repository.place.TgUserPlaceRepository;
import ru.rassafel.bot.session.repository.place.VkUserPlaceRepository;
import ru.rassafel.foodsharing.common.model.PlatformType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final TgUserPlaceRepository tgUserPlaceRepository;
    private final VkUserPlaceRepository vkUserPlaceRepository;

    public Collection<Place> findByUserId(Long id, PlatformType platformType) {
        if (platformType == PlatformType.TG) {
            return new ArrayList<>(tgUserPlaceRepository.findByUserId(id));
        } else {
            return new ArrayList<>(vkUserPlaceRepository.findByUserId(id));
        }
    }

    public void save(Place place, PlatformType platformType) {
        if (platformType == PlatformType.TG) {
            tgUserPlaceRepository.save((TgUserPlace) place);
        } else {
            vkUserPlaceRepository.save((VkUserPlace) place);
        }
    }

    public Map<Integer, String> getUsersPlacesNamesMap(User user, PlatformType platformType) {
        Collection<Place> usersPlaces = findByUserId(user.getId(), platformType);
        return getUsersPlacesNamesMap(usersPlaces);
    }

    public Map<Integer, String> getUsersPlacesNamesMap(Collection<Place> places) {
        int[] productCounter = {1};
        return places.stream().map(Place::getName).collect(Collectors.toMap(
            o -> productCounter[0]++,
            o -> o
        ));
    }

    public String getUsersPlaceMapMessage(User user, PlatformType platformType, String additionalMessage) {
        return getUsersPlacesNamesMap(user, platformType).entrySet().stream().map(entry -> entry.getKey() + "." + entry.getValue())
            .collect(Collectors.joining("\n"))
            + "\n\n" + additionalMessage;
    }

    public String getUsersPlaceMapMessage(User user, PlatformType platformType) {
        return getUsersPlaceMapMessage(user, platformType, "");
    }


    public void deletePlace(Place place, PlatformType platformType) {
        if (platformType == PlatformType.TG) {
            tgUserPlaceRepository.delete((TgUserPlace) place);
        } else if (platformType == PlatformType.VK) {
            vkUserPlaceRepository.delete((VkUserPlace) place);
        }
    }
}

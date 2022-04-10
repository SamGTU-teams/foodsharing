package ru.rassafel.foodsharing.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.common.model.PlatformType;
import ru.rassafel.foodsharing.common.model.entity.geo.Place;
import ru.rassafel.foodsharing.common.model.entity.geo.TgUserPlace;
import ru.rassafel.foodsharing.common.model.entity.geo.VkUserPlace;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.repository.place.TgUserPlaceRepository;
import ru.rassafel.foodsharing.common.repository.place.VkUserPlaceRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final TgUserPlaceRepository tgUserPlaceRepository;
    private final VkUserPlaceRepository vkUserPlaceRepository;

    public Collection<Place> findByUserId(Long id, PlatformType platformType){
        if(platformType == PlatformType.TG){
            return new ArrayList<>(tgUserPlaceRepository.findByUserId(id));
        }else {
            return new ArrayList<>(vkUserPlaceRepository.findByUserId(id));
        }
    }

    public void save(Place place, PlatformType platformType){
        if(platformType == PlatformType.TG){
            tgUserPlaceRepository.save((TgUserPlace) place);
        }else {
            vkUserPlaceRepository.save((VkUserPlace) place);
        }
    }

    public Map<Integer, String> getUsersPlacesNamesMap(User user, PlatformType platformType){
        int[] productCounter = {1};
        Collection<Place> usersPlaces = findByUserId(user.getId(), platformType);
        Map<Integer, String> collect = usersPlaces.stream().map(Place::getName).collect(Collectors.toMap(
            o -> productCounter[0]++,
            o -> o
        ));
        return collect;
    }

    public String getUsersPlaceMapMessage(User user, PlatformType platformType, String additionalMessage){
        return getUsersPlacesNamesMap(user, platformType).entrySet().stream().map(entry -> entry.getKey() + "." + entry.getValue())
            .collect(Collectors.joining("\n"))
            + "\n\n" + additionalMessage;
    }

    public String getUsersPlaceMapMessage(User user, PlatformType platformType){
        return getUsersPlaceMapMessage(user, platformType, "");
    }



    public void deletePlace(Place place, PlatformType platformType){
        if(platformType == PlatformType.TG){
            tgUserPlaceRepository.delete((TgUserPlace) place);
        } else if (platformType == PlatformType.VK){
            vkUserPlaceRepository.delete((VkUserPlace) place);
        }
    }
}

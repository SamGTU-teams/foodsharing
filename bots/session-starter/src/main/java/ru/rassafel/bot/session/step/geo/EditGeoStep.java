package ru.rassafel.bot.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.model.PlatformType;
import ru.rassafel.foodsharing.common.model.entity.geo.Place;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.foodsharing.common.service.PlaceService;

import java.util.Collection;
import java.util.Optional;

@Component("geo-6")
@RequiredArgsConstructor
public class EditGeoStep implements Step {

    private final Cache<Long, Place> geoPointCache;
    private final PlaceService placeService;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        final String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();
        Collection<Place> usersPoints = placeService.findByUserId(user.getId(), sessionRequest.getType());
        Place point;
        if(message.matches("\\d+")){
            String placeNameToEdit = placeService.getUsersPlacesNamesMap(user, PlatformType.VK).get(Integer.parseInt(message));
            point = usersPoints.stream().filter(p -> p.getName().equalsIgnoreCase(placeNameToEdit)).findFirst().orElseThrow(() ->
                new RuntimeException("Uncaught exception, place name not found in user places!"));
        }else {
            point = usersPoints.stream().filter(p -> p.getName().equalsIgnoreCase(message)).findFirst().orElseThrow(() ->
                new BotException(user.getId(), "Такого места нет, попробуйте еще"));
        }
        geoPointCache.put(user.getId(), point);
        sessionResponse.setMessage("Укажите новый радиус");
        sessionResponse.setButtons(new BotButtons());
        userSession.setSessionStep(7);
    }
}

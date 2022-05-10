package ru.rassafel.bot.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.exception.ApiException;

import java.util.Collection;
import java.util.Map;

@Component("geo-6")
@RequiredArgsConstructor
public class EditGeoStep implements Step {
    private final Cache<Long, Place> geoPointCache;
    private final PlaceService placeService;
    private final UserService userService;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        final String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();
        Collection<Place> usersPoints = placeService.findByUserId(user.getId());
        Place point;
        if (message.matches("\\d+")) {
            Map<Integer, String> usersPlacesNamesMap = placeService.getUsersPlacesNamesMap(usersPoints);
            String placeNameToEdit = usersPlacesNamesMap.get(Integer.parseInt(message));
            if (placeNameToEdit == null) {
                throw new BotException(user.getId(), "Такого номера места у вас нет, попробуйте еще");
            }
            point = usersPoints.stream().filter(p -> p.getName().equalsIgnoreCase(placeNameToEdit)).findFirst().orElseThrow(() ->
                new ApiException("Uncaught exception, place name not found in user places!"));
        } else {
            point = usersPoints.stream().filter(p -> p.getName().equalsIgnoreCase(message)).findFirst().orElseThrow(() ->
                new BotException(user.getId(), "Такого названия места нет, попробуйте еще"));
        }
        geoPointCache.put(user.getId(), point);
        sessionResponse.setMessage("Укажите новый радиус");
        sessionResponse.setButtons(new BotButtons());
        userSession.setSessionStep(7);

        userService.saveUser(user);
    }
}

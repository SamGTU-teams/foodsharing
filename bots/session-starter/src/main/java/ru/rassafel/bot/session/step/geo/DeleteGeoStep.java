package ru.rassafel.bot.session.step.geo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.model.entity.place.Place;
import ru.rassafel.bot.session.model.entity.user.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.user.User;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.util.GeoButtonsUtil;
import ru.rassafel.bot.session.util.SessionUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Component("geo-5")
@RequiredArgsConstructor
public class DeleteGeoStep implements Step {

    private final PlaceService placeService;
    private final UserService userService;

    @Override
    @Transactional
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();
        BotButtons responseButtons = new BotButtons();

        Map<Integer, String> usersPlacesNamesMap = placeService.getUsersPlacesNamesMap(user, sessionRequest.getType());
        Set<String> placesNamesToDelete;
        try {
            placesNamesToDelete = SessionUtil.getAllNames(usersPlacesNamesMap, message);
        } catch (IllegalArgumentException ex) {
            throw new BotException(user.getId(), ex.getMessage());
        }
        Collection<Place> usersPlaces = placeService.findByUserId(user.getId(), sessionRequest.getType());
        for (String placeName : placesNamesToDelete) {
            Place place = usersPlaces.stream().filter(p -> p.getName().equals(placeName)).findFirst()
                .orElseThrow(() -> new RuntimeException("Uncaught error! Place name not found"));
            placeService.deletePlace(place, sessionRequest.getType());
            usersPlaces.removeIf(p -> p.getName().equalsIgnoreCase(placeName));
        }
        if (usersPlaces.isEmpty()) {
            sessionResponse.setMessage("Место удалено, у вас больше не осталось мест");
            userSession.setSessionStep(1);
            responseButtons.addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS);

            userService.saveUser(user);
        } else {
            String otherPlaces = placeService.getUsersPlaceMapMessage(user, sessionRequest.getType());
            sessionResponse.setMessage("Место удалено, введите еще, оставшиеся места:\n\n" + otherPlaces);
        }
        sessionResponse.setButtons(responseButtons);
    }
}
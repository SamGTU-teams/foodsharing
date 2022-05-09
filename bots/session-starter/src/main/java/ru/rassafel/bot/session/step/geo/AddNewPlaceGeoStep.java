package ru.rassafel.bot.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.model.dto.LocationDto;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.OpenStreetMapService;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.step.Step;

@Component("geo-2")
@RequiredArgsConstructor
public class AddNewPlaceGeoStep implements Step {
    private final Cache<Long, Place> geoPointCache;
    private final UserService userService;
    private final PlaceService placeService;
    private final OpenStreetMapService streetMapService;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        BotButtons responseButtons = new BotButtons();
        if (sessionRequest.getLocation() == null || sessionRequest.getLocation().getLatitude() == 0 || sessionRequest.getLocation().getLongitude() == 0) {
            sessionResponse.setMessage("Мне необходима геолокация!");
            responseButtons.addButton(BotButtons.BotButton.GEO_BUTTON);
        } else {
            LocationDto location = sessionRequest.getLocation();

            Place place = placeService.createPlace(user, location);
            String address =
                streetMapService.getAddress(location.getLatitude(), location.getLongitude());

            geoPointCache.put(user.getId(), place);

            sessionResponse.setMessage("Теперь дайте название этому месту" +
                "\nПримечание: лучше не использовать цифровые названия");

            userSession.setSessionStep(3);

            userService.saveUser(user);
        }

        sessionResponse.setButtons(responseButtons);
    }
}

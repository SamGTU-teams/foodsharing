package ru.rassafel.bot.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.AddressService;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.service.message.TemplateEngine;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.templates.PlaceTemplates;
import ru.rassafel.foodsharing.common.model.GeoPoint;

@Component("geo-2")
@RequiredArgsConstructor
public class AddNewPlaceGeoStep implements Step {

    public static final int STEP_INDEX = 2;

    private final Cache<Long, Place> geoPointCache;
    private final UserService userService;
    private final PlaceService placeService;
    private final AddressService streetMapService;

    private final TemplateEngine templateEngine;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        BotButtons responseButtons = new BotButtons();
        GeoPoint location = sessionRequest.getLocation();
        if (location == null || location.getLat() == 0 || location.getLon() == 0) {
            sessionResponse.setMessage("Мне необходима геолокация!");
            responseButtons.addButton(BotButtons.BotButton.GEO_BUTTON);
        } else {
            String address =
                streetMapService.getAddress(location.getLat(), location.getLon());

            Place place = placeService.createPlace(user, location, address);

            geoPointCache.put(user.getId(), place);

            sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.EXPECTATION_OF_GEO_NAME));

            userSession.setSessionStep(SetNameGeoStep.STEP_INDEX);

            userService.saveUser(user);
        }

        sessionResponse.setButtons(responseButtons);
    }
}

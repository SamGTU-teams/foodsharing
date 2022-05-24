package ru.rassafel.foodsharing.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.common.model.GeoPoint;
import ru.rassafel.foodsharing.session.model.dto.BotButtons;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.EmbeddedUserSession;
import ru.rassafel.foodsharing.session.model.entity.Place;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.service.PlaceService;
import ru.rassafel.foodsharing.session.service.UserService;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.openmap.AddressService;
import ru.rassafel.foodsharing.session.step.Step;
import ru.rassafel.foodsharing.session.templates.PlaceTemplates;

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
        if (location == null) {
            sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.NO_GEOLOCATION_PROVIDED));
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

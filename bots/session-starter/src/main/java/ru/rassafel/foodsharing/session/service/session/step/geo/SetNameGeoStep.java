package ru.rassafel.foodsharing.session.service.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.session.exception.BotException;
import ru.rassafel.foodsharing.session.model.dto.BotButtons;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.EmbeddedUserSession;
import ru.rassafel.foodsharing.session.model.entity.Place;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.service.PlaceService;
import ru.rassafel.foodsharing.session.service.UserService;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.session.step.Step;
import ru.rassafel.foodsharing.session.service.message.templates.MainTemplates;
import ru.rassafel.foodsharing.session.service.message.templates.PlaceTemplates;
import ru.rassafel.foodsharing.session.util.button.GeoButtonsUtil;

import java.util.Collection;
import java.util.Map;

import static ru.rassafel.foodsharing.session.util.button.ButtonsUtil.DEFAULT_BUTTONS;
import static ru.rassafel.foodsharing.session.util.button.GeoButtonsUtil.BACK_TO_PLACES;

@Component("geo-3")
@RequiredArgsConstructor
public class SetNameGeoStep implements Step {
    public static final int STEP_INDEX = 3;

    private final Cache<Long, Place> geoPointCache;
    private final PlaceService placeService;
    private final UserService userService;
    private final TemplateEngine templateEngine;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        String message = sessionRequest.getMessage();
        Place place = geoPointCache.getIfPresent(user.getId());

        if (BACK_TO_PLACES.equalsIgnoreCase(sessionRequest.getMessage())) {
            userSession.setSessionStep(ChooseOperationGeoStep.STEP_INDEX);
            sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.BACK_TO_PLACES));
            sessionResponse.setButtons(new BotButtons().addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS));
            geoPointCache.invalidate(user.getId());
            userService.saveUser(user);
            return;
        }

        if (place == null) {
            userSession.setSessionActive(false);
            sessionResponse.setButtons(new BotButtons(DEFAULT_BUTTONS));
            sessionResponse.setMessage(templateEngine.compileTemplate(MainTemplates.OPERATION_TIMEOUT));
            userService.saveUser(user);
            return;
        }

        if (message.isEmpty()) {
            throw new BotException(user.getId(), templateEngine.compileTemplate(PlaceTemplates.PLACE_NAME_IS_EMPTY));
        }

        if (message.length() > 63) {
            throw new BotException(user.getId(), templateEngine.compileTemplate(PlaceTemplates.TOO_MANY_PLACE_NAME));
        }

        Collection<? extends Place> usersPlaces = placeService.findByUserId(user.getId());
        String upperedPlaceName = StringUtils.capitalize(message);
        if (usersPlaces.stream().anyMatch(p -> p.getName().equalsIgnoreCase(message))) {
            throw new BotException(user.getId(),
                templateEngine.compileTemplate(PlaceTemplates.PLACE_ALREADY_EXISTS, Map.of("name", message)));
        }

        place.setName(upperedPlaceName);

        sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.EXPECTATION_OF_RADIUS));
        sessionResponse.setButtons(new BotButtons().addButton(new BotButtons.BotButton(BACK_TO_PLACES))
            .addButton(new BotButtons.BotButton(GeoButtonsUtil.LEAVE_RADIUS_AS_IS)));
        userSession.setSessionStep(SetRadiusAndFinishSaveGeoStep.STEP_INDEX);

        userService.saveUser(user);
    }
}

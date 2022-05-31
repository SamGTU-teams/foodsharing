package ru.rassafel.foodsharing.session.service.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.common.exception.ApiException;
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
import ru.rassafel.foodsharing.session.service.message.templates.PlaceTemplates;
import ru.rassafel.foodsharing.session.util.button.GeoButtonsUtil;

import java.util.Collection;
import java.util.Map;

import static ru.rassafel.foodsharing.session.util.button.GeoButtonsUtil.BACK_TO_PLACES;

@Component("geo-6")
@RequiredArgsConstructor
public class EditGeoStep implements Step {
    public static final int STEP_INDEX = 6;

    private final Cache<Long, Place> geoPointCache;
    private final PlaceService placeService;
    private final UserService userService;
    private final TemplateEngine templateEngine;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        final String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();
        Collection<? extends Place> usersPoints = placeService.findByUserId(user.getId());
        if (BACK_TO_PLACES.equalsIgnoreCase(sessionRequest.getMessage())) {
            userSession.setSessionStep(ChooseOperationGeoStep.STEP_INDEX);
            sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.BACK_TO_PLACES));
            sessionResponse.setButtons(new BotButtons().addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS));
            geoPointCache.invalidate(user.getId());
            userService.saveUser(user);
            return;
        }
        Place point;
        if (message.matches("^\\d+$")) {
            Map<Integer, String> usersPlacesNamesMap = placeService.getUsersPlacesNamesMap(usersPoints);
            String placeNameToEdit = usersPlacesNamesMap.get(Integer.parseInt(message));
            if (placeNameToEdit == null) {
                throw new BotException(user.getId(),
                    templateEngine.compileTemplate(PlaceTemplates.PLACE_BY_NUM_NOT_FOUND, Map.of("number", message)));
            }
            point = usersPoints.stream().filter(p -> p.getName().equalsIgnoreCase(placeNameToEdit)).findFirst().orElseThrow(() ->
                new ApiException("Uncaught exception, place name not found in user places!"));
        } else {
            point = usersPoints.stream().filter(p -> p.getName().equalsIgnoreCase(message)).findFirst().orElseThrow(() ->
                new BotException(user.getId(),
                    templateEngine.compileTemplate(PlaceTemplates.PLACE_WITH_NAME_NOT_FOUND, Map.of("name", message))));
        }
        geoPointCache.put(user.getId(), point);
        sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.EXPECTATION_OF_NEW_RADIUS));
        sessionResponse.setButtons(new BotButtons().addButton(new BotButtons.BotButton(BACK_TO_PLACES)));
        userSession.setSessionStep(SetNewRadiusGeoStep.STEP_INDEX);

        userService.saveUser(user);
    }
}

package ru.rassafel.foodsharing.session.service.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
import ru.rassafel.foodsharing.session.util.button.SessionUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static ru.rassafel.foodsharing.session.util.button.GeoButtonsUtil.BACK_TO_PLACES;

@Component("geo-5")
@RequiredArgsConstructor
public class DeleteGeoStep implements Step {
    public static final int STEP_INDEX = 5;

    private final PlaceService placeService;
    private final UserService userService;
    private final TemplateEngine templateEngine;
    private final Cache<Long, Place> geoPointCache;

    @Override
    @Transactional
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();
        BotButtons responseButtons = new BotButtons();

        if (BACK_TO_PLACES.equalsIgnoreCase(sessionRequest.getMessage())) {
            userSession.setSessionStep(ChooseOperationGeoStep.STEP_INDEX);
            sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.BACK_TO_PLACES));
            responseButtons.addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS);
            geoPointCache.invalidate(user.getId());
            userService.saveUser(user);
        } else if (message.equalsIgnoreCase(GeoButtonsUtil.DELETE_ALL)) {
            placeService.deleteAllByUserId(user.getId());

            responseButtons.addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS);
            userSession.setSessionStep(ChooseOperationGeoStep.STEP_INDEX);
            sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.EMPTY_PLACES_AFTER_DELETE));
            userService.saveUser(user);
        } else {

            Map<Integer, String> usersPlacesNamesMap = placeService.getUsersPlacesNamesMap(user);
            Set<String> placesNamesToDelete;
            try {
                placesNamesToDelete = SessionUtil.findValuesByMessage(usersPlacesNamesMap, message);
            } catch (IllegalArgumentException ex) {
                throw new BotException(user.getId(), ex.getMessage());
            }
            Collection<? extends Place> usersPlaces = placeService.findByUserId(user.getId());
            for (String placeName : placesNamesToDelete) {
                Place place = usersPlaces.stream().filter(p -> p.getName().equalsIgnoreCase(placeName)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Uncaught error! Place name not found"));
                placeService.deletePlace(place);
                usersPlaces.removeIf(p -> p.getName().equalsIgnoreCase(placeName));
            }
            if (usersPlaces.isEmpty()) {
                sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.EMPTY_PLACES_AFTER_DELETE));
                userSession.setSessionStep(ChooseOperationGeoStep.STEP_INDEX);
                responseButtons.addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS);

                userService.saveUser(user);
            } else {
                sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.PLACES_LIST_AFTER_DELETE,
                    PlaceTemplates.buildMapOfPlaces(usersPlaces)));
                responseButtons.addButton(new BotButtons.BotButton(GeoButtonsUtil.BACK_TO_PLACES));
            }
        }
        sessionResponse.setButtons(responseButtons);
    }
}

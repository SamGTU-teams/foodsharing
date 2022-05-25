package ru.rassafel.foodsharing.session.step.geo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import ru.rassafel.foodsharing.session.step.Step;
import ru.rassafel.foodsharing.session.templates.MainTemplates;
import ru.rassafel.foodsharing.session.templates.PlaceTemplates;

import java.util.Collection;
import java.util.Map;

import static ru.rassafel.foodsharing.session.util.GeoButtonsUtil.*;

@Component("geo-1")
@RequiredArgsConstructor
public class ChooseOperationGeoStep implements Step {
    public static final int STEP_INDEX = 1;

    private final PlaceService placeService;
    private final UserService userService;
    private final TemplateEngine templateEngine;
    @Value("${bot-config.max-places-count:10}")
    private Integer maxPlacesCount;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        Collection<Place> points = placeService.findByUserId(user.getId());
        if (message.equalsIgnoreCase(MY_PLACES)) {
            if (points.isEmpty()) {
                responseMessage = templateEngine.compileTemplate(PlaceTemplates.EMPTY_PLACES);
            } else {
                responseMessage = templateEngine.compileTemplate(PlaceTemplates.PLACES_LIST,
                    PlaceTemplates.buildMapOfPlaces(points));
            }

            responseButtons.addAll(GEO_MAIN_BUTTONS);

        } else if (message.equalsIgnoreCase(ADD_PLACE)) {
            if (points.size() >= maxPlacesCount) {
                throw new BotException(user.getId(), templateEngine.compileTemplate(PlaceTemplates.TOO_MANY_PLACES,
                    Map.of("count", maxPlacesCount)));
            }
            responseMessage = templateEngine.compileTemplate(PlaceTemplates.EXPECTATION_OF_GEO);
            responseButtons.addButton(BotButtons.BotButton.GEO_BUTTON);

            userSession.setSessionStep(AddNewPlaceGeoStep.STEP_INDEX);
        } else if (message.equalsIgnoreCase(DELETE_PLACE)) {
            if (points.isEmpty()) {

                responseMessage = templateEngine.compileTemplate(PlaceTemplates.EMPTY_PLACES);
                responseButtons.addAll(GEO_MAIN_BUTTONS);

            } else {
                responseMessage = templateEngine.compileTemplate(PlaceTemplates.PLACES_LIST_TO_DELETE,
                    PlaceTemplates.buildMapOfPlaces(points));

                responseButtons.addButton(new BotButtons.BotButton(DELETE_ALL));
                userSession.setSessionStep(DeleteGeoStep.STEP_INDEX);
            }
        } else if (message.equalsIgnoreCase(EDIT_PLACE)) {

            if (points.isEmpty()) {
                responseMessage = templateEngine.compileTemplate(PlaceTemplates.EMPTY_PLACES);
                responseButtons.addAll(GEO_MAIN_BUTTONS);
            } else {
                responseMessage = templateEngine.compileTemplate(PlaceTemplates.PLACES_LIST_TO_EDIT,
                    PlaceTemplates.buildMapOfPlaces(placeService.findByUserId(user.getId())));

                userSession.setSessionStep(EditGeoStep.STEP_INDEX);
            }
        } else {
            throw new BotException(user.getId(), templateEngine.compileTemplate(MainTemplates.INVALID_OPERATION,
                MainTemplates.buildMapOfOperations(GEO_MAIN_BUTTONS)));
        }

        sessionResponse.setButtons(responseButtons);
        sessionResponse.setMessage(responseMessage);

        userService.saveUser(user);
    }
}

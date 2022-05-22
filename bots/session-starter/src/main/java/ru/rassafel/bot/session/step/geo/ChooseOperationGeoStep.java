package ru.rassafel.bot.session.step.geo;

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
import ru.rassafel.bot.session.service.message.TemplateEngine;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.templates.MainTemplates;
import ru.rassafel.bot.session.templates.PlaceTemplates;

import java.util.Collection;

import static ru.rassafel.bot.session.util.GeoButtonsUtil.GEO_MAIN_BUTTONS;

@Component("geo-1")
@RequiredArgsConstructor
public class ChooseOperationGeoStep implements Step {

    public static final int STEP_INDEX = 1;

    private final PlaceService placeService;
    private final UserService userService;

    private final TemplateEngine templateEngine;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        String responseMessage;
        BotButtons responseButtons = new BotButtons();

        if (message.equals("мои места")) {
            Collection<Place> points = placeService.findByUserId(user.getId());
            if (points.isEmpty()) {

                responseMessage = templateEngine.compileTemplate(PlaceTemplates.EMPTY_PLACES);

            } else {
                responseMessage = templateEngine.compileTemplate(PlaceTemplates.PLACES_LIST,
                    PlaceTemplates.buildMapOfPlaces(points));
            }

            responseButtons.addAll(GEO_MAIN_BUTTONS);

        } else if (message.equals("добавить место")) {

            responseMessage = templateEngine.compileTemplate(PlaceTemplates.EXPECTATION_OF_GEO);
            responseButtons.addButton(BotButtons.BotButton.GEO_BUTTON);

            userSession.setSessionStep(AddNewPlaceGeoStep.STEP_INDEX);
        } else if (message.equals("удалить место")) {
            Collection<Place> points = placeService.findByUserId(user.getId());
            if (points.isEmpty()) {

                responseMessage = templateEngine.compileTemplate(PlaceTemplates.EMPTY_PLACES);
                responseButtons.addAll(GEO_MAIN_BUTTONS);

            } else {
                responseMessage = templateEngine.compileTemplate(PlaceTemplates.PLACES_LIST_TO_DELETE,
                    PlaceTemplates.buildMapOfPlaces(points));

                responseButtons.addButton(new BotButtons.BotButton("Удалить все"));
                userSession.setSessionStep(DeleteGeoStep.STEP_INDEX);
            }
        } else if (message.equals("редактирование места")) {
            Collection<Place> points = placeService.findByUserId(user.getId());

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

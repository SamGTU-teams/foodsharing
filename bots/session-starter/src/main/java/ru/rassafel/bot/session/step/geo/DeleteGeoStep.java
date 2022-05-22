package ru.rassafel.bot.session.step.geo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
import ru.rassafel.bot.session.templates.PlaceTemplates;
import ru.rassafel.bot.session.util.GeoButtonsUtil;
import ru.rassafel.bot.session.util.SessionUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Component("geo-5")
@RequiredArgsConstructor
public class DeleteGeoStep implements Step {

    public static final int STEP_INDEX = 5;

    private final PlaceService placeService;
    private final UserService userService;

    private final TemplateEngine templateEngine;

    @Override
    @Transactional
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();
        BotButtons responseButtons = new BotButtons();

        if(message.equalsIgnoreCase("удалить все")){
            placeService.deleteAllByUserId(user.getId());
            userService.saveUser(user);

            responseButtons.addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS);
            userSession.setSessionStep(ChooseOperationGeoStep.STEP_INDEX);
            sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.EMPTY_PLACES_AFTER_DELETE));
        }else {

            Map<Integer, String> usersPlacesNamesMap = placeService.getUsersPlacesNamesMap(user);
            Set<String> placesNamesToDelete;
            try {
                placesNamesToDelete = SessionUtil.findValuesByMessage(usersPlacesNamesMap, message);
            } catch (IllegalArgumentException ex) {
                throw new BotException(user.getId(), ex.getMessage());
            }
            Collection<Place> usersPlaces = placeService.findByUserId(user.getId());
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
            }
        }
        sessionResponse.setButtons(responseButtons);
    }
}

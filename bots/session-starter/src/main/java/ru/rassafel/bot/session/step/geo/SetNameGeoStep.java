package ru.rassafel.bot.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
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
import ru.rassafel.bot.session.util.GeoButtonsUtil;

import java.util.Collection;
import java.util.Map;

import static ru.rassafel.bot.session.util.ButtonsUtil.DEFAULT_BUTTONS;

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

        if (place == null) {
            userSession.setSessionActive(false);
            sessionResponse.setButtons(new BotButtons(DEFAULT_BUTTONS));
            sessionResponse.setMessage(templateEngine.compileTemplate(MainTemplates.OPERATION_TIMEOUT));
            userService.saveUser(user);
            return;
        }

        if (message.length() > 63) {
            throw new BotException(user.getId(), templateEngine.compileTemplate(PlaceTemplates.TOO_MANY_PLACE_NAME));
        }

        Collection<Place> usersPlaces = placeService.findByUserId(user.getId());

        if (usersPlaces.stream().anyMatch(p -> p.getName().equalsIgnoreCase(message))) {
            throw new BotException(user.getId(),
                templateEngine.compileTemplate(PlaceTemplates.PLACE_ALREADY_EXISTS, Map.of("name", message)));
        }

        place.setName(message);

        sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.EXPECTATION_OF_RADIUS));
        sessionResponse.setButtons(new BotButtons().addButton(new BotButtons.BotButton(GeoButtonsUtil.LEAVE_RADIUS_AS_IS)));
        userSession.setSessionStep(SetRadiusAndFinishSaveGeoStep.STEP_INDEX);

        userService.saveUser(user);
    }
}

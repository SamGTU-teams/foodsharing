package ru.rassafel.foodsharing.session.service.session.step.geo;


import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
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

import static ru.rassafel.foodsharing.session.util.button.ButtonsUtil.DEFAULT_BUTTONS;
import static ru.rassafel.foodsharing.session.util.button.GeoButtonsUtil.*;

@Component("geo-4")
@RequiredArgsConstructor
public class SetRadiusAndFinishSaveGeoStep implements Step {
    public static final int STEP_INDEX = 4;

    private final Cache<Long, Place> geoPointCache;
    private final PlaceService placeService;
    private final UserService userService;
    private final PlatformTransactionManager transactionManager;
    private final TemplateEngine templateEngine;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        String message = sessionRequest.getMessage();

        if (BACK_TO_PLACES.equalsIgnoreCase(sessionRequest.getMessage())) {
            userSession.setSessionStep(ChooseOperationGeoStep.STEP_INDEX);
            sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.BACK_TO_PLACES));
            sessionResponse.setButtons(new BotButtons().addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS));
            geoPointCache.invalidate(user.getId());
            userService.saveUser(user);
            return;
        }

        Place place = geoPointCache.getIfPresent(user.getId());

        if (place == null) {
            sessionResponse.setButtons(new BotButtons(DEFAULT_BUTTONS));
            userSession.setSessionActive(false);
            sessionResponse.setMessage(templateEngine.compileTemplate(MainTemplates.OPERATION_TIMEOUT));
            userService.saveUser(user);
            return;
        }

        BotButtons responseButtons = new BotButtons();

        int radius;

        if (message.equalsIgnoreCase(LEAVE_RADIUS_AS_IS)) {
            radius = 1000;
        } else {
            try {
                radius = Integer.parseInt(message);
                if (radius > 5000 || radius <= 0) {
                    throw new BotException(user.getId(), templateEngine.compileTemplate(PlaceTemplates.INVALID_RADIUS_RANGE));
                }
            } catch (NumberFormatException ex) {
                throw new BotException(user.getId(),
                    templateEngine.compileTemplate(PlaceTemplates.INVALID_RADIUS_FORMAT));
            }
        }

        place.setRadius(radius);
        geoPointCache.invalidate(user.getId());

        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setName("FinishSaveGeoStep.executeStep");

        template.executeWithoutResult(transactionStatus -> {
            placeService.save(place);
            userSession.setSessionStep(ChooseOperationGeoStep.STEP_INDEX);
            userService.saveUser(user);
        });

        sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.FINISHED_SAVE_POINT));

        sessionResponse.setButtons(responseButtons.addAll(GEO_MAIN_BUTTONS));
    }
}

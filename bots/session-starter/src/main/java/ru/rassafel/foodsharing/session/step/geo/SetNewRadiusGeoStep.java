package ru.rassafel.foodsharing.session.step.geo;

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
import ru.rassafel.foodsharing.session.step.Step;
import ru.rassafel.foodsharing.session.templates.MainTemplates;
import ru.rassafel.foodsharing.session.templates.PlaceTemplates;
import ru.rassafel.foodsharing.session.util.GeoButtonsUtil;

import static ru.rassafel.foodsharing.session.util.ButtonsUtil.DEFAULT_BUTTONS;

@Component("geo-7")
@RequiredArgsConstructor
public class SetNewRadiusGeoStep implements Step {
    public static final int STEP_INDEX = 7;

    private final Cache<Long, Place> geoPointCache;
    private final PlaceService placeService;
    private final UserService userService;
    private final PlatformTransactionManager transactionManager;
    private final TemplateEngine templateEngine;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        Place editable = geoPointCache.getIfPresent(user.getId());

        if (editable == null) {
            userSession.setSessionActive(false);
            sessionResponse.setButtons(new BotButtons(DEFAULT_BUTTONS));
            sessionResponse.setMessage(templateEngine.compileTemplate(MainTemplates.OPERATION_TIMEOUT));
            userService.saveUser(user);
            return;
        }

        int newRadius;
        try {
            newRadius = Integer.parseInt(message);
            if (newRadius > 5000 || newRadius <= 0) {
                throw new BotException(user.getId(), templateEngine.compileTemplate(PlaceTemplates.INVALID_RADIUS_RANGE));
            }
        } catch (NumberFormatException ex) {
            throw new BotException(user.getId(), templateEngine.compileTemplate(PlaceTemplates.INVALID_RADIUS_FORMAT));
        }
        geoPointCache.invalidate(user.getId());

        editable.setRadius(newRadius);
        userSession.setSessionStep(ChooseOperationGeoStep.STEP_INDEX);

        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setName("SetNewRadiusGeoStep.executeStep");

        template.executeWithoutResult(transactionStatus -> {
            placeService.save(editable);
            userService.saveUser(user);
        });

        sessionResponse.setButtons(new BotButtons().addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS));
        sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.PLACE_EDIT_SUCCESS));
    }
}

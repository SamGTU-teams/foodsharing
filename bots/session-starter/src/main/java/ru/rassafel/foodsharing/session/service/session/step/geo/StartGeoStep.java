package ru.rassafel.foodsharing.session.service.session.step.geo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.session.model.dto.BotButtons;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.EmbeddedUserSession;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.service.UserService;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.session.step.Step;
import ru.rassafel.foodsharing.session.service.message.templates.PlaceTemplates;
import ru.rassafel.foodsharing.session.util.button.GeoButtonsUtil;

@Component("geo-0")
@RequiredArgsConstructor
public class StartGeoStep implements Step {
    private final UserService userService;
    private final TemplateEngine templateEngine;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        sessionResponse.setMessage(templateEngine.compileTemplate(PlaceTemplates.CHOOSE_GEO_OPERATION));
        BotButtons responseButtons = new BotButtons();
        sessionResponse.setButtons(responseButtons.addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS));
        userSession.setSessionStep(ChooseOperationGeoStep.STEP_INDEX);

        userService.saveUser(user);
    }
}

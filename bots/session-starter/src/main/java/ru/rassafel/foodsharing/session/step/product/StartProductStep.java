package ru.rassafel.foodsharing.session.step.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.session.model.dto.BotButtons;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.EmbeddedUserSession;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.service.UserService;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.step.Step;
import ru.rassafel.foodsharing.session.templates.ProductTemplates;

import static ru.rassafel.foodsharing.session.util.ProductButtonsUtil.PRODUCT_MAIN_BUTTONS;

@Component("product-0")
@RequiredArgsConstructor
public class StartProductStep implements Step {
    private final UserService userService;
    private final TemplateEngine templateEngine;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        sessionResponse.setMessage(templateEngine.compileTemplate(ProductTemplates.CHOOSE_PRODUCT_OPERATION));
        BotButtons responseButtons = new BotButtons();
        sessionResponse.setButtons(responseButtons.addAll(PRODUCT_MAIN_BUTTONS));
        userSession.setSessionStep(ChooseOperationProductStep.STEP_INDEX);
        userService.saveUser(user);
    }
}

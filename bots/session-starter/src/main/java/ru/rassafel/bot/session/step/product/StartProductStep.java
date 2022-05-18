package ru.rassafel.bot.session.step.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.service.message.TemplateEngine;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.templates.ProductTemplates;

import static ru.rassafel.bot.session.util.ProductButtonsUtil.PRODUCT_MAIN_BUTTONS;

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

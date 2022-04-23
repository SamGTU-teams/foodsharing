package ru.rassafel.bot.session.step.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.util.GeoButtonsUtil;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.service.UserService;

import static ru.rassafel.bot.session.util.ProductButtonsUtil.PRODUCT_MAIN_BUTTONS;

@Component("product-0")
@RequiredArgsConstructor
public class StartProductStep implements Step {

    private final UserService userService;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {

        EmbeddedUserSession userSession = user.getUserSession();
        sessionResponse.setMessage("Выберите:");
        BotButtons responseButtons = new BotButtons();
        sessionResponse.setButtons(responseButtons.addAll(PRODUCT_MAIN_BUTTONS));
        userSession.setSessionStep(1);
        userService.saveUser(user);
    }
}

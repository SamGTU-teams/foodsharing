package ru.rassafel.bot.session.step.geo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.util.GeoButtonsUtil;

@Component("geo-0")
@RequiredArgsConstructor
public class StartGeoStep implements Step {
    private final UserService userService;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        sessionResponse.setMessage("Выберите:");
        BotButtons responseButtons = new BotButtons();
        sessionResponse.setButtons(responseButtons.addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS));
        userSession.setSessionStep(1);

        userService.saveUser(user);
    }
}

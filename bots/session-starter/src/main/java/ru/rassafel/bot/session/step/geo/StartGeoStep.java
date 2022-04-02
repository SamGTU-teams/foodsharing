package ru.rassafel.bot.session.step.geo;

import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.util.ButtonsUtil;
import ru.rassafel.bot.session.util.GeoButtonsUtil;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;

import java.util.List;

@Component("geo-0")
public class StartGeoStep implements Step {

    @Override
    public Integer getStepNumber() {
        return 0;
    }

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        sessionResponse.setMessage("Выберите: ");
        sessionResponse.setButtons(ButtonsUtil.withBackToMain(GeoButtonsUtil.GEO_MAIN_BUTTONS));
        userSession.setSessionStep(1);
    }
}

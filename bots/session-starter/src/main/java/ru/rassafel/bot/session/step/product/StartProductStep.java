package ru.rassafel.bot.session.step.product;

import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.model.entity.user.User;

@Component("product-0")
public class StartProductStep implements Step {
    @Override
    public Integer getStepNumber() {
        return null;
    }

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {

    }
}

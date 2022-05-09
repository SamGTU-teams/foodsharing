package ru.rassafel.bot.session.type.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.dto.To;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.step.StepFinder;
import ru.rassafel.bot.session.type.BotSession;

@Component("productSession")
@RequiredArgsConstructor
public class ProductSession implements BotSession {
    private final StepFinder stepFinder;

    @Override
    public SessionResponse execute(SessionRequest request, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        int step = userSession.getSessionStep();
        final SessionResponse response = new SessionResponse();

        stepFinder.execute(step, request, response, user, "product");

        return response.toBuilder()
            .sendTo(To.builder()
                .id(user.getId())
                .build())
            .build();
    }

    @Override
    public String getName() {
        return "productSession";
    }
}

package ru.rassafel.foodsharing.session.type.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.dto.To;
import ru.rassafel.foodsharing.session.model.entity.EmbeddedUserSession;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.step.StepFinder;
import ru.rassafel.foodsharing.session.type.BotSession;

@RequiredArgsConstructor
@Component(GeoSession.NAME)
public class GeoSession implements BotSession {
    public static final String NAME = "geoSession";
    private final StepFinder stepFinder;

    @Override
    public SessionResponse execute(SessionRequest request, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        int step = userSession.getSessionStep();
        final SessionResponse response = new SessionResponse();

        stepFinder.execute(step, request, response, user, "geo");

        return response.toBuilder()
            .sendTo(To.builder()
                .id(user.getId())
                .build())
            .build();
    }

    @Override
    public String getName() {
        return NAME;
    }
}

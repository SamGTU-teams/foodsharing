package ru.rassafel.bot.session.type.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.dto.To;
import ru.rassafel.bot.session.step.geo.GeoSteps;
import ru.rassafel.bot.session.type.BotSession;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;

@Component
@RequiredArgsConstructor
public class GeoSession implements BotSession {

    private final GeoSteps geoSteps;

    @Override
    public SessionResponse execute(SessionRequest request, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        int step = userSession.getSessionStep();
        final SessionResponse response = new SessionResponse();

        geoSteps.execute(step, request, response, user);

        return response.toBuilder()
                .sendTo(To.builder()
                        .id(user.getId())
                        .build())
                .build();
    }

    @Override
    public String getName() {
        return "geoSession";
    }

}

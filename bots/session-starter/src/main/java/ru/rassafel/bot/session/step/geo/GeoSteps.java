package ru.rassafel.bot.session.step.geo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.model.entity.user.User;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GeoSteps {

    private final Map<String, Step> stepMap;

    public void execute(Integer step, SessionRequest request, SessionResponse sessionResponse, User user) {
        Optional.ofNullable(stepMap.get("geo-" + step)).orElseThrow(() -> new IllegalStateException("Can't find step bean!"))
                .executeStep(request, sessionResponse, user);
    }


}

package ru.rassafel.bot.session.step;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.User;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StepFinder {
    private final Map<String, Step> stepMap;

    public void execute(Integer step, SessionRequest request, SessionResponse sessionResponse, User user, String stepPrefix) {
        Optional.ofNullable(stepMap.get(stepPrefix + "-" + step)).orElseThrow(() -> new IllegalStateException("Can't find step bean!"))
            .executeStep(request, sessionResponse, user);
    }
}

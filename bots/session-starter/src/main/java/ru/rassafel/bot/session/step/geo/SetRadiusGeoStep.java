package ru.rassafel.bot.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.ExpiredLocationObjectException;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.model.entity.geo.Place;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.bot.session.util.ButtonsUtil;

import java.util.Optional;

@Component("geo-3")
@RequiredArgsConstructor
public class SetRadiusGeoStep implements Step {

    private final Cache<Long, Place> geoPointCache;

    @Override
    public Integer getStepNumber() {
        return 3;
    }

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        String message = sessionRequest.getMessage();
        Place place = geoPointCache.getIfPresent(user.getId());

        if (place == null) {
            sessionResponse.setButtons(ButtonsUtil.DEFAULT_BUTTONS);
            userSession.setSessionActive(false);
            sessionResponse.setMessage("Время данной операции истекло");
            return;
        }

        place.setName(message);

        sessionResponse.setMessage("Отлично, а теперь укажите радиус поиска вокруг этого места");
        sessionResponse.setButtons(ButtonsUtil.BACK_TO_MAIN);
        userSession.setSessionStep(4);
    }
}

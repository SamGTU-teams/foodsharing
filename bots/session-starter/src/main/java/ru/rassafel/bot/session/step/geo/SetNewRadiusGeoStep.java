package ru.rassafel.bot.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.foodsharing.common.model.entity.geo.Place;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.foodsharing.common.service.PlaceService;

@Component("geo-7")
@RequiredArgsConstructor
public class SetNewRadiusGeoStep implements Step {

    private final Cache<Long, Place> geoPointCache;
    private final PlaceService placeService;

    @Override
    public Integer getStepNumber() {
        return 7;
    }

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        int newRadius = Integer.parseInt(message);
        EmbeddedUserSession userSession = user.getUserSession();

        Place editable = geoPointCache.getIfPresent(user.getId());
        editable.setRadius(newRadius);
        placeService.save(editable, sessionRequest.getType());
        geoPointCache.invalidate(user.getId());

        sessionResponse.setButtons(null);
        sessionResponse.setMessage("Место изменено!");
        userSession.setSessionStep(1);
    }
}

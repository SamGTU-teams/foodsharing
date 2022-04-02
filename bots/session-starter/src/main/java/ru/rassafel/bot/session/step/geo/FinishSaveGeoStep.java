package ru.rassafel.bot.session.step.geo;


import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.exception.ExpiredLocationObjectException;
import ru.rassafel.bot.session.util.ButtonsUtil;
import ru.rassafel.bot.session.util.GeoButtonsUtil;
import ru.rassafel.foodsharing.common.model.entity.geo.Place;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.foodsharing.common.service.PlaceService;

import java.util.Optional;

@Component("geo-4")
@RequiredArgsConstructor
public class FinishSaveGeoStep implements Step {

    private final Cache<Long, Place> geoPointCache;
    private final PlaceService placeService;

    @Override
    public Integer getStepNumber() {
        return 4;
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


        int radius;
        try {
            radius = Integer.parseInt(message);
        } catch (NumberFormatException ex) {
            throw new BotException(user.getId(), "Введите число");
        }

        place.setRadius(radius);

        placeService.save(place, sessionRequest.getType());

        sessionResponse.setMessage("Круто! Место сохранено");

        geoPointCache.invalidate(user.getId());

        userSession.setSessionStep(1);

        sessionResponse.setButtons(ButtonsUtil.withBackToMain(GeoButtonsUtil.GEO_MAIN_BUTTONS));
    }
}

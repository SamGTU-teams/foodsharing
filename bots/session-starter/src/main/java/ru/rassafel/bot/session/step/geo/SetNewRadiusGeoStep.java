package ru.rassafel.bot.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.util.GeoButtonsUtil;
import ru.rassafel.foodsharing.common.model.entity.geo.Place;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.foodsharing.common.service.PlaceService;

import static ru.rassafel.bot.session.util.ButtonsUtil.DEFAULT_BUTTONS;

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
        EmbeddedUserSession userSession = user.getUserSession();

        Place editable = geoPointCache.getIfPresent(user.getId());

        if (editable == null) {
            userSession.setSessionActive(false);
            sessionResponse.setButtons(new BotButtons(DEFAULT_BUTTONS));
            sessionResponse.setMessage("Время данной операции истекло");
            return;
        }

        int newRadius;
        try {
            newRadius = Integer.parseInt(message);
            if (newRadius >= 5000) {
                throw new BotException(user.getId(), "Нужно ввести радиус меньше 5000");
            }
        } catch (NumberFormatException ex) {
            throw new BotException(user.getId(), "Нужно ввести число");
        }


        editable.setRadius(newRadius);
        placeService.save(editable, sessionRequest.getType());
        geoPointCache.invalidate(user.getId());

        sessionResponse.setButtons(new BotButtons().addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS));
        sessionResponse.setMessage("Место изменено!");
        userSession.setSessionStep(1);
    }
}

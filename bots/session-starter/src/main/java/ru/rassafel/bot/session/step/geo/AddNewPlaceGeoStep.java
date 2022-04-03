package ru.rassafel.bot.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.LocationDto;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.model.PlatformType;
import ru.rassafel.foodsharing.common.model.entity.geo.*;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;

@Component("geo-2")
@RequiredArgsConstructor
public class AddNewPlaceGeoStep implements Step {

    private final Cache<Long, Place> geoPointCache;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        BotButtons responseButtons = new BotButtons();
        if (sessionRequest.getLocation() == null || sessionRequest.getLocation().getLatitude() == 0 || sessionRequest.getLocation().getLongitude() == 0) {
            sessionResponse.setMessage("Мне необходима геолокация!");
            responseButtons.addButton(BotButtons.BotButton.GEO_BUTTON);
        } else {
            LocationDto location = sessionRequest.getLocation();

            Place place;
            if(sessionRequest.getType() == PlatformType.TG){
                place = new TgUserPlace()
                    .withUser(user)
                    .withGeo(new GeoPointEmbeddable(location.getLatitude(), location.getLongitude()));
            }else if(sessionRequest.getType() == PlatformType.VK){
                place = new VkUserPlace()
                    .withUser(user)
                    .withGeo(new GeoPointEmbeddable(location.getLatitude(), location.getLongitude()));
            }else {
                throw new IllegalArgumentException("No platformType found!");
            }

            geoPointCache.put(user.getId(), place);

            sessionResponse.setMessage("Теперь дайте название этому месту");

            userSession.setSessionStep(3);
        }

        sessionResponse.setButtons(responseButtons);
    }
}

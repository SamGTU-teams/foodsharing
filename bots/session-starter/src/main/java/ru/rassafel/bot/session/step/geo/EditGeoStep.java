package ru.rassafel.bot.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.foodsharing.common.model.entity.geo.Place;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.foodsharing.common.service.PlaceService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component("geo-6")
@RequiredArgsConstructor
public class EditGeoStep implements Step {

    private final Cache<Long, Place> geoPointCache;
    private final PlaceService placeRepository;

    @Override
    public Integer getStepNumber() {
        return 6;
    }

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        final String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();
        Collection<Place> usersPoints = placeRepository.findByUserId(user.getId(), sessionRequest.getType());
        Optional<Place> point = usersPoints.stream().filter(p -> p.getName().equalsIgnoreCase(message)).findFirst();
        if (point.isEmpty()) {
            sessionResponse.setMessage("Такого места нет");
            return;
        }
        geoPointCache.put(user.getId(), point.get());
        sessionResponse.setMessage("Укажите радиус");
        sessionResponse.setButtons(List.of("На главную"));
        userSession.incrementStep();
    }
}

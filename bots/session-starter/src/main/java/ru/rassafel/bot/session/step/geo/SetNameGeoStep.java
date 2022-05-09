package ru.rassafel.bot.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.step.Step;

import java.util.Collection;

import static ru.rassafel.bot.session.util.ButtonsUtil.DEFAULT_BUTTONS;

@Component("geo-3")
@RequiredArgsConstructor
public class SetNameGeoStep implements Step {
    private final Cache<Long, Place> geoPointCache;
    private final PlaceService placeService;
    private final UserService userService;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        String message = sessionRequest.getMessage();
        Place place = geoPointCache.getIfPresent(user.getId());

        if (place == null) {
            userSession.setSessionActive(false);
            sessionResponse.setButtons(new BotButtons(DEFAULT_BUTTONS));
            sessionResponse.setMessage("Время данной операции истекло");
            userService.saveUser(user);
            return;
        }

        Collection<Place> usersPlaces = placeService.findByUserId(user.getId());

        if (usersPlaces.stream().anyMatch(p -> p.getName().equalsIgnoreCase(message))) {
            throw new BotException(user.getId(), "Введите другое название, место " + message + " у вас уже есть");
        }

        place.setName(message);

        sessionResponse.setMessage("Отлично, а теперь укажите радиус поиска вокруг этого места, по умолчанию радиус будет указан в 1 км");
        sessionResponse.setButtons(new BotButtons().addButton(new BotButtons.BotButton("Оставить как есть")));
        userSession.setSessionStep(4);

        userService.saveUser(user);
    }
}

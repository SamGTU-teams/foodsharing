package ru.rassafel.bot.session.step.geo;


import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.model.entity.place.Place;
import ru.rassafel.bot.session.model.entity.user.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.user.User;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.step.Step;

import static ru.rassafel.bot.session.util.ButtonsUtil.DEFAULT_BUTTONS;
import static ru.rassafel.bot.session.util.GeoButtonsUtil.GEO_MAIN_BUTTONS;

@Component("geo-4")
@RequiredArgsConstructor
public class SetRadiusAndFinishSaveGeoStep implements Step {
    private final Cache<Long, Place> geoPointCache;
    private final PlaceService placeService;
    private final UserService userService;
    private final PlatformTransactionManager transactionManager;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        EmbeddedUserSession userSession = user.getUserSession();
        String message = sessionRequest.getMessage();

        Place place = geoPointCache.getIfPresent(user.getId());

        if (place == null) {
            sessionResponse.setButtons(new BotButtons(DEFAULT_BUTTONS));
            userSession.setSessionActive(false);
            sessionResponse.setMessage("Время данной операции истекло");
            userService.saveUser(user);
            return;
        }

        BotButtons responseButtons = new BotButtons();

        int radius;

        if (message.equals("оставить как есть")) {
            radius = 1000;
        } else {
            try {
                radius = Integer.parseInt(message);
                if (radius >= 5000) {
                    throw new BotException(user.getId(), "Нужно ввести радиус меньше 5 км");
                }
            } catch (NumberFormatException ex) {
                throw new BotException(user.getId(), "Введите число");
            }
        }

        place.setRadius(radius);
        geoPointCache.invalidate(user.getId());

        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setName("FinishSaveGeoStep.executeStep");

        template.executeWithoutResult(transactionStatus -> {
            placeService.save(place, sessionRequest.getType());
            userSession.setSessionStep(1);
            userService.saveUser(user);
        });

        sessionResponse.setMessage("Круто! Место сохранено");

        sessionResponse.setButtons(responseButtons.addAll(GEO_MAIN_BUTTONS));
    }
}

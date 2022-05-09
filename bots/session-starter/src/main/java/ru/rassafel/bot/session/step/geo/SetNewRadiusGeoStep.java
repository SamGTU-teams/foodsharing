package ru.rassafel.bot.session.step.geo;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.exception.BotException;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.PlaceService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.step.Step;
import ru.rassafel.bot.session.util.GeoButtonsUtil;

import static ru.rassafel.bot.session.util.ButtonsUtil.DEFAULT_BUTTONS;

@Component("geo-7")
@RequiredArgsConstructor
public class SetNewRadiusGeoStep implements Step {
    private final Cache<Long, Place> geoPointCache;
    private final PlaceService placeService;
    private final UserService userService;
    private final PlatformTransactionManager transactionManager;

    @Override
    public void executeStep(SessionRequest sessionRequest, SessionResponse sessionResponse, User user) {
        String message = sessionRequest.getMessage();
        EmbeddedUserSession userSession = user.getUserSession();

        Place editable = geoPointCache.getIfPresent(user.getId());

        if (editable == null) {
            userSession.setSessionActive(false);
            sessionResponse.setButtons(new BotButtons(DEFAULT_BUTTONS));
            sessionResponse.setMessage("Время данной операции истекло");
            userService.saveUser(user);
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
        userSession.setSessionStep(1);

        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setName("SetNewRadiusGeoStep.executeStep");

        template.executeWithoutResult(transactionStatus -> {
            placeService.save(editable);
            userService.saveUser(user);
        });

        sessionResponse.setButtons(new BotButtons().addAll(GeoButtonsUtil.GEO_MAIN_BUTTONS));
        geoPointCache.invalidate(user.getId());
        sessionResponse.setMessage("Место изменено!");
    }
}

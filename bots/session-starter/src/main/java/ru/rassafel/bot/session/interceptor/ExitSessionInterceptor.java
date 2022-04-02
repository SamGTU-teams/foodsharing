package ru.rassafel.bot.session.interceptor;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.dto.To;
import ru.rassafel.bot.session.service.FilePropertiesService;
import ru.rassafel.bot.session.type.BotSession;
import ru.rassafel.foodsharing.common.model.entity.geo.Place;
import ru.rassafel.foodsharing.common.model.entity.user.EmbeddedUserSession;
import ru.rassafel.foodsharing.common.model.entity.user.User;
import ru.rassafel.bot.session.util.ButtonsUtil;

@Component
@RequiredArgsConstructor
public class ExitSessionInterceptor implements SessionExecutorInterceptor {

    private final FilePropertiesService filePropertiesService;
    private final Cache<Long, Place> geoPointCache;

    public SessionResponse handle(SessionRequest request, User user, BotSession next) {
        final String userMsg = request.getMessage();

        if (userMsg.equals(filePropertiesService.getButtonName("back-to-main")) && user.getUserSession() != null) {

            geoPointCache.invalidate(user.getId());

            EmbeddedUserSession userSession = user.getUserSession();
            userSession.setSessionActive(false);
            return SessionResponse.builder()
                    .message(filePropertiesService.getSessionMessage("back-to-main"))
                    .sendTo(new To(user.getId()))
                    .buttons(ButtonsUtil.DEFAULT_BUTTONS)
                    .build();
        }
        return next.execute(request, user);
    }

}

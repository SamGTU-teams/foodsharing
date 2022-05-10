package ru.rassafel.bot.session.interceptor.impl;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.interceptor.SessionExecutorInterceptor;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.dto.To;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.FilePropertiesService;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.type.BotSession;
import ru.rassafel.bot.session.util.ButtonsUtil;

@Component
@RequiredArgsConstructor
public class ExitSessionInterceptor implements SessionExecutorInterceptor {
    private final FilePropertiesService filePropertiesService;
    private final Cache<Long, Place> geoPointCache;
    private final UserService userService;

    @Override
    public SessionResponse handle(SessionRequest request, User user, BotSession next) {
        final String userMsg = request.getMessage();

        EmbeddedUserSession userSession = user.getUserSession();
        if (userMsg.equals(filePropertiesService.getButtonName("back-to-main")) && userSession != null) {
            geoPointCache.invalidate(user.getId());

            userSession.setSessionActive(false);
            userService.saveUser(user);
            return SessionResponse.builder()
                .message(filePropertiesService.getSessionMessage("back-to-main"))
                .sendTo(new To(user.getId()))
                .buttons(new BotButtons(ButtonsUtil.DEFAULT_BUTTONS))
                .build();
        }
        return next.execute(request, user);
    }
}

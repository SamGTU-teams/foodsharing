package ru.rassafel.foodsharing.session.interceptor.impl;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.session.interceptor.SessionInterceptor;
import ru.rassafel.foodsharing.session.model.dto.BotButtons;
import ru.rassafel.foodsharing.session.model.dto.SessionRequest;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.dto.To;
import ru.rassafel.foodsharing.session.model.entity.EmbeddedUserSession;
import ru.rassafel.foodsharing.session.model.entity.Place;
import ru.rassafel.foodsharing.session.model.entity.User;
import ru.rassafel.foodsharing.session.service.UserService;
import ru.rassafel.foodsharing.session.service.message.TemplateEngine;
import ru.rassafel.foodsharing.session.service.message.templates.MainTemplates;
import ru.rassafel.foodsharing.session.service.session.type.BotSession;

import static ru.rassafel.foodsharing.session.util.button.ButtonsUtil.BACK_TO_MAIN_MENU;
import static ru.rassafel.foodsharing.session.util.button.ButtonsUtil.DEFAULT_BUTTONS;

@Component
@RequiredArgsConstructor
public class ExitSessionInterceptor implements SessionInterceptor {
    private final Cache<Long, Place> geoPointCache;
    private final UserService userService;
    private final TemplateEngine templateEngine;

    @Override
    public SessionResponse handle(SessionRequest request, User user, BotSession next) {
        final String userMsg = request.getMessage();

        EmbeddedUserSession userSession = user.getUserSession();
        if (userMsg != null && userMsg.equalsIgnoreCase(BACK_TO_MAIN_MENU) && userSession != null) {
            geoPointCache.invalidate(user.getId());

            userSession.setSessionActive(false);
            userService.saveUser(user);
            return SessionResponse.builder()
                .message(templateEngine.compileTemplate(MainTemplates.BACK_TO_MAIN))
                .sendTo(new To(user.getId()))
                .buttons(new BotButtons(DEFAULT_BUTTONS))
                .build();
        }
        return next.execute(request, user);
    }
}

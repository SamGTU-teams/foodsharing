package ru.rassafel.bot.session.interceptor.impl;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.interceptor.SessionInterceptor;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.dto.To;
import ru.rassafel.bot.session.model.entity.EmbeddedUserSession;
import ru.rassafel.bot.session.model.entity.Place;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.UserService;
import ru.rassafel.bot.session.service.message.TemplateEngine;
import ru.rassafel.bot.session.templates.MainTemplates;
import ru.rassafel.bot.session.type.BotSession;
import ru.rassafel.bot.session.util.ButtonsUtil;

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
        if (userMsg != null && userMsg.equalsIgnoreCase("На главную") && userSession != null) {
            geoPointCache.invalidate(user.getId());

            userSession.setSessionActive(false);
            userService.saveUser(user);
            return SessionResponse.builder()
                .message(templateEngine.compileTemplate(MainTemplates.BACK_TO_MAIN))
                .sendTo(new To(user.getId()))
                .buttons(new BotButtons(ButtonsUtil.DEFAULT_BUTTONS))
                .build();
        }
        return next.execute(request, user);
    }
}

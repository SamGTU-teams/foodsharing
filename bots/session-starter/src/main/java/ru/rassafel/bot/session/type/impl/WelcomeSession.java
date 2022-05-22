package ru.rassafel.bot.session.type.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.dto.To;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.message.TemplateEngine;
import ru.rassafel.bot.session.templates.MainTemplates;
import ru.rassafel.bot.session.type.BotSession;
import ru.rassafel.bot.session.util.ButtonsUtil;

@RequiredArgsConstructor
@Component(WelcomeSession.NAME)
public class WelcomeSession implements BotSession {
    public static final String NAME = "welcomeSession";
    private final TemplateEngine templateEngine;

    @Override
    public SessionResponse execute(SessionRequest request, User user) {
        return SessionResponse
            .builder()
            .buttons(new BotButtons(ButtonsUtil.DEFAULT_BUTTONS))
            .message(templateEngine.compileTemplate(MainTemplates.WELCOME, null))
            .sendTo(To.builder()
                .id(user.getId())
                .build())
            .build();
    }

    @Override
    public String getName() {
        return NAME;
    }
}

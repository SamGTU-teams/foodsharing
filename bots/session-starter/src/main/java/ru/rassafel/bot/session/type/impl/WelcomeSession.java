package ru.rassafel.bot.session.type.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.model.dto.BotButtons;
import ru.rassafel.bot.session.model.dto.SessionRequest;
import ru.rassafel.bot.session.model.dto.SessionResponse;
import ru.rassafel.bot.session.model.dto.To;
import ru.rassafel.bot.session.model.entity.User;
import ru.rassafel.bot.session.service.FilePropertiesService;
import ru.rassafel.bot.session.type.BotSession;
import ru.rassafel.bot.session.util.ButtonsUtil;

@RequiredArgsConstructor
@Component(WelcomeSession.NAME)
public class WelcomeSession implements BotSession {
    public static final String NAME = "welcomeSession";
    @Autowired
    private FilePropertiesService propertiesService;

    @Override
    public SessionResponse execute(SessionRequest request, User user) {
        return SessionResponse
            .builder()
            .buttons(new BotButtons(ButtonsUtil.DEFAULT_BUTTONS))
            .message(propertiesService.getSessionMessage("welcome"))
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

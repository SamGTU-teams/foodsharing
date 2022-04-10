package ru.rassafel.bot.session.type.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.dto.SessionRequest;
import ru.rassafel.bot.session.dto.SessionResponse;
import ru.rassafel.bot.session.dto.To;
import ru.rassafel.bot.session.model.BotButtons;
import ru.rassafel.bot.session.service.FilePropertiesService;
import ru.rassafel.bot.session.type.BotSession;
import ru.rassafel.bot.session.util.ButtonsUtil;
import ru.rassafel.foodsharing.common.model.entity.user.User;

@Component
public class WelcomeSession implements BotSession {

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
        return "welcomeSession";
    }
}

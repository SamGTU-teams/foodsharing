package ru.rassafel.bot.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.rassafel.bot.session.type.BotSession;
import ru.rassafel.bot.session.type.impl.WelcomeSession;

@Component
@RequiredArgsConstructor
public class WelcomeSessionProducer {
    private final ApplicationContext context;

    public BotSession getWelcome() {
        return context.getBean(WelcomeSession.class);
    }
}

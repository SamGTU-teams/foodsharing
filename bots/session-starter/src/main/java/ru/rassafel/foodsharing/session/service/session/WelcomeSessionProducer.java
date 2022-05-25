package ru.rassafel.foodsharing.session.service.session;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.rassafel.foodsharing.session.type.BotSession;
import ru.rassafel.foodsharing.session.type.impl.WelcomeSession;

@Component
@RequiredArgsConstructor
public class WelcomeSessionProducer {
    private final ApplicationContext context;

    public BotSession getWelcome() {
        return context.getBean(WelcomeSession.class);
    }
}

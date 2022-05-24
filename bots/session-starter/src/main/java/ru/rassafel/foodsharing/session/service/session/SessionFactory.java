package ru.rassafel.foodsharing.session.service.session;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.foodsharing.session.type.BotSession;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionFactory {
    private final Map<String, BotSession> sessionMap;

    public BotSession getSession(String firstMessage) {
        return sessionMap.get(SessionEnum.getBeanName(firstMessage));
    }

    public BotSession getSessionByName(String name) {
        return sessionMap.get(name);
    }
}

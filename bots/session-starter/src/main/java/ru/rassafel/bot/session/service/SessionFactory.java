package ru.rassafel.bot.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rassafel.bot.session.type.BotSession;

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

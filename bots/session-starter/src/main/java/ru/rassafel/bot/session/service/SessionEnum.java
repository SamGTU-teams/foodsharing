package ru.rassafel.bot.session.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.rassafel.bot.session.type.impl.GeoSession;
import ru.rassafel.bot.session.type.impl.ProductSession;
import ru.rassafel.bot.session.type.impl.WelcomeSession;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum SessionEnum {
    WELCOME("/start", WelcomeSession.NAME),
    PRODUCT("продукты", ProductSession.NAME),
    GEO("места", GeoSession.NAME);

    private final String message;
    private final String beanName;

    public static String getBeanName(String message) {
        return Arrays.stream(values())
            .map(SessionEnum::getMessage)
            .filter(s -> Objects.equals(s, message))
            .findFirst()
            .map(SessionEnum::getBeanName)
            .orElseThrow(IllegalArgumentException::new);
    }
}

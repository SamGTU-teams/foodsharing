package ru.rassafel.bot.session.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum SessionEnum {
    WELCOME("/start", "welcomeSession"),
    PRODUCT("продукты", "productSession"),
    GEO("места", "geoSession");

    private final String message;
    private final String beanName;

    public static String getBeanName(String message) {
        return Arrays.stream(values()).filter(o -> o.getMessage().
            equals(message)).findFirst().map(SessionEnum::getBeanName).orElseThrow(IllegalArgumentException::new);
    }
}

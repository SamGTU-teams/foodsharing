package ru.rassafel.foodsharing.session.service.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.rassafel.foodsharing.session.service.session.type.impl.GeoSession;
import ru.rassafel.foodsharing.session.service.session.type.impl.ProductSession;
import ru.rassafel.foodsharing.session.service.session.type.impl.WelcomeSession;

import java.util.Arrays;

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
            .filter(v -> v.getMessage().equalsIgnoreCase(message))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Session name not found"))
            .getBeanName();
    }
}

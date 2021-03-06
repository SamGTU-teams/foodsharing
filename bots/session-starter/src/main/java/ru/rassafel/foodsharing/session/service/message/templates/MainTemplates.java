package ru.rassafel.foodsharing.session.service.message.templates;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public enum MainTemplates implements Templates {
    WELCOME("welcome"),
    INVALID_OPERATION("invalid-operation-name"),
    OPERATION_TIMEOUT("operation-timeout"),
    POST_INFO("food-post-info"),
    ERROR_ON_SERVER("error-on-server"),
    BACK_TO_MAIN("back-to-main");

    private final String name;

    public static Map<String, List<String>> buildMapOfOperations(List<String> operationNames) {
        return Map.of("operations", operationNames);
    }

    @Override
    public String getName() {
        return name;
    }
}

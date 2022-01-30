package ru.rassafel.foodsharing.vkparser.service.impl;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

/**
 * @author rassafel
 */
class SecretMatches {
    static Stream<Arguments> registerWithoutAccessMatchValues() {
        return Stream.<Arguments>builder()
            .add(Arguments.of(null, null))
            .add(Arguments.of("Test secret", "Test secret"))
            .build();
    }

    static Stream<Arguments> registerWithoutAccessNotMatchValues() {
        return Stream.<Arguments>builder()
            .add(Arguments.of("Test secret", null))
            .add(Arguments.of(null, "Test secret"))
            .add(Arguments.of("Test secret", "Not match secret"))
            .build();
    }
}

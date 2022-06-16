package ru.rassafel.foodsharing.analyzer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author rassafel
 */
@SpringBootTest
@ActiveProfiles("integration-test")
class AnalyzerApplicationIntegrationTest {
    @Test
    void contextLoads() {
        System.out.println("Context loaded.");
    }
}

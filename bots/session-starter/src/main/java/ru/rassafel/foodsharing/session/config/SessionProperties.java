package ru.rassafel.foodsharing.session.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @author rassafel
 */
@Data
@ConfigurationProperties(prefix = SessionProperties.PREFIX, ignoreUnknownFields = false)
public class SessionProperties {
    public static final String PREFIX = "bot.session";

    private Cache placesCache = new Cache();
    private boolean enabled = true;

    @Data
    public static class Cache {
        private Duration expirationTime = Duration.of(10, ChronoUnit.MINUTES);
    }
}

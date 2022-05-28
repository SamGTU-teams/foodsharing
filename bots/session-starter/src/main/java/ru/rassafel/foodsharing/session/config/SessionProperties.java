package ru.rassafel.foodsharing.session.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private PlaceProperties place = new PlaceProperties();
    private CallbackProperties callback = new CallbackProperties();
    private boolean enabled = true;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CacheProperties {
        private Duration expirationTime = Duration.of(10, ChronoUnit.MINUTES);
    }

    @Data
    public static class PlaceProperties {
        private CacheProperties cache = new CacheProperties(Duration.of(10, ChronoUnit.MINUTES));
    }

    @Data
    public static class CallbackProperties {
        private CacheProperties cache = new CacheProperties(Duration.of(1, ChronoUnit.MINUTES));
    }
}

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
    private MessengerConfigs messenger = new MessengerConfigs();

    @Data
    public static class Cache {
        private Duration expirationTime = Duration.of(10, ChronoUnit.MINUTES);
    }

    @Data
    public static class MessengerConfigs {
        private int maxRequestQueueSize = 100;
        private int maxThreadCountForSendQueries = 1;
        private int maxTimeForSendSomeQueries = 1000;
        private int maxQueryCountPerTime = 20;
        private int maxQuerySizeInBatch = 25;
    }
}

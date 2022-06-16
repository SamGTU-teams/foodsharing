package ru.rassafel.foodsharing.vkparser.config;

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
@ConfigurationProperties(prefix = VkParserProperties.PREFIX)
public class VkParserProperties {
    public static final String PREFIX = "vk.parser";

    private String callbackUrl;
    private String serverTitle;
    private Client client = new Client();
    private CallbackProperties callback = new CallbackProperties();

    @Data
    public static class Client {
        private int retryAttemptsInternalServerErrorCount = 3;
        private int retryAttemptsNetworkErrorCount = 3;
        private int retryAttemptsInvalidStatusCount = 3;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cache {
        private Duration expirationTime = Duration.of(10, ChronoUnit.MINUTES);
    }

    @Data
    public static class CallbackProperties {
        private Cache cache = new Cache(Duration.of(2, ChronoUnit.DAYS));
    }
}

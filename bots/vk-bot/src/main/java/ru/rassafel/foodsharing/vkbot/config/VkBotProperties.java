package ru.rassafel.foodsharing.vkbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@Data
@ConfigurationProperties(prefix = VkBotProperties.PREFIX)
public class VkBotProperties {
    public static final String PREFIX = "vk.bot";

    private int groupId;

    private String accessToken;

    private String callbackUrl;

    private String serverTitle;

    private Client client = new Client();

    @Data
    public static class Client {
        private int retryAttemptsInternalServerErrorCount = 3;

        private int retryAttemptsNetworkErrorCount = 3;

        private int retryAttemptsInvalidStatusCount = 3;

        private int maxRequestQueueSize = 100;

        private int maxThreadCountForSendQueries = 1;

        private int maxTimeForSendSomeQueries = 1000;

        private int maxQueryCountPerTime = 20;

        private int maxQuerySizeInBatch = 25;
    }
}

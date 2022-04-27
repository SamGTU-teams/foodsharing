package ru.rassafel.foodsharing.vkparser.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@Data
@ConfigurationProperties(prefix = VkParserProperties.PREFIX)
public class VkParserProperties {
    public static final String PREFIX = "vk.parser";

    private String callbackUrl;

    private String serverTitle;

    private int retryAttemptsInternalServerErrorCount = 3;

    private int retryAttemptsNetworkErrorCount = 3;

    private int retryAttemptsInvalidStatusCount = 3;
}

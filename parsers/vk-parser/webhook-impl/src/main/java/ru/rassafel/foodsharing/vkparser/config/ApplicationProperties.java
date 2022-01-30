package ru.rassafel.foodsharing.vkparser.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@Getter
@Setter
@ConfigurationProperties(prefix = ApplicationProperties.PREFIX)
public class ApplicationProperties {
    public static final String PREFIX = "application";

    private String url;

    private String serverTitle;

    private int retryAttemptsInternalServerErrorCount = 3;

    private int retryAttemptsNetworkErrorCount = 3;

    private int retryAttemptsInvalidStatusCount = 3;
}

package ru.rassafel.foodsharing.vkparser.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.rassafel.foodsharing.vkparser.controller.CallbackController;

/**
 * @author rassafel
 */
@Getter
@Setter
@ConfigurationProperties(prefix = ApplicationProperties.PREFIX)
public class ApplicationProperties {
    public static final String PREFIX = "application";

    private String url;

    private String callbackUrl = url.replaceAll("/+$", "") + CallbackController.MAPPING;

    private String serverTitle;

    private int retryAttemptsInternalServerErrorCount = 3;

    private int retryAttemptsNetworkErrorCount = 3;

    private int retryAttemptsInvalidStatusCount = 3;
}

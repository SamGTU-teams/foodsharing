package ru.rassafel.foodsharing.ibot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@Data
@ConfigurationProperties(prefix = IBotProperties.PREFIX)
public class IBotProperties {
    public static final String PREFIX = "feign.ibot";

    private String url = "";
}

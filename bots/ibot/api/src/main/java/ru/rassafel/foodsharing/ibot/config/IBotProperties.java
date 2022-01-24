package ru.rassafel.foodsharing.ibot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@Getter
@Setter
@ConfigurationProperties(prefix = IBotProperties.PREFIX)
public class IBotProperties {
    public static final String PREFIX = "feign.ibot";

    private String url = "";
}

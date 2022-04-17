package ru.rassafel.bot.session.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author rassafel
 */
@Data
@ConfigurationProperties(prefix = SessionProperties.PREFIX)
public class SessionProperties {
    public static final String PREFIX = "bot.session";

    private Duration expirationTime;
}

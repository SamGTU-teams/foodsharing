package ru.rassafel.bot.session.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author rassafel
 */
@ConfigurationProperties(prefix = SessionProperties.PREFIX)
@Setter
@Getter
public class SessionProperties {
    public static final String PREFIX = "bot.session";

    private Duration expirationTime;
}

package ru.rassafel.foodsharing.tgbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@Data
@ConfigurationProperties(prefix = TgBotProperties.PREFIX)
public class TgBotProperties {
    public static final String PREFIX = "tg.bot";

    private String token;
    private String botUsername;
    private String webHookPath;
}

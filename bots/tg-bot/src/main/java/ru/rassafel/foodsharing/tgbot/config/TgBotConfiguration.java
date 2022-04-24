package ru.rassafel.foodsharing.tgbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import ru.rassafel.foodsharing.tgbot.service.TgBotHandlerService;

@Configuration
@ConfigurationProperties(prefix = "tg.bot")
@Getter
@Setter
public class TgBotConfiguration {

    private String token;
    private String webHookPath;
    private String botUsername;

    @Bean
    public TgBotHandlerService foodSharingWebHookBot() {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        options.setProxyHost("localhost");
        options.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        options.setProxyPort(9150);
        return new TgBotHandlerService(botUsername, botUsername, webHookPath);
    }

}

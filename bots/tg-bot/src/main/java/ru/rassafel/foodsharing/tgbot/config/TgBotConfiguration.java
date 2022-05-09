package ru.rassafel.foodsharing.tgbot.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import ru.rassafel.foodsharing.tgbot.mapper.TgBotDtoMapper;
import ru.rassafel.foodsharing.tgbot.service.TgBotHandlerService;

@Configuration
@EnableConfigurationProperties(TgBotProperties.class)
@EntityScan(basePackages = "ru.rassafel.foodsharing.tgbot.model")
public class TgBotConfiguration {
    @Bean
    TgBotDtoMapper tgBotDtoMapper() {
        return TgBotDtoMapper.INSTANCE;
    }

    @Bean
    public TgBotHandlerService foodSharingWebHookBot(TgBotProperties properties) {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        options.setProxyHost("localhost");
        options.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        options.setProxyPort(9150);
        return new TgBotHandlerService(properties.getBotUsername(), properties.getToken(), properties.getWebHookPath());
    }
}

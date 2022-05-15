package ru.rassafel.foodsharing.tgbot.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.rassafel.foodsharing.tgbot.mapper.TgBotDtoMapper;
import ru.rassafel.foodsharing.tgbot.service.TgBotHandlerService;

@Configuration
@EnableConfigurationProperties(TgBotProperties.class)
@EntityScan(basePackages = "ru.rassafel.foodsharing.tgbot.model")
@EnableScheduling
public class TgBotConfiguration {
    @Bean
    TgBotDtoMapper tgBotDtoMapper() {
        return TgBotDtoMapper.INSTANCE;
    }

    @Bean
    public TgBotHandlerService foodSharingWebHookBot(TgBotProperties properties) {
        return new TgBotHandlerService(properties.getBotUsername(),
            properties.getToken(),
            properties.getWebHookPath());
    }
}

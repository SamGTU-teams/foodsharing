package ru.rassafel.foodsharing.tgbot.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rassafel.foodsharing.tgbot.model.mapper.TgBotDtoMapper;

@Configuration
@EnableConfigurationProperties(TgBotProperties.class)
@EntityScan(basePackages = "ru.rassafel.foodsharing.tgbot.model.domain")
public class TgBotConfiguration {
    @Bean
    TgBotDtoMapper tgBotDtoMapper() {
        return TgBotDtoMapper.INSTANCE;
    }

    @Bean
    TelegramWebhookBot foodSharingWebHookBot(TgBotProperties properties) {
        return new TelegramWebhookBot() {
            @Override
            public String getBotToken() {
                return properties.getToken();
            }

            @Override
            public String getBotUsername() {
                return properties.getBotUsername();
            }

            @Override
            public String getBotPath() {
                return properties.getWebHookPath();
            }

            @Override
            public BotApiMethod onWebhookUpdateReceived(Update update) {
                return null;
            }
        };
    }
}

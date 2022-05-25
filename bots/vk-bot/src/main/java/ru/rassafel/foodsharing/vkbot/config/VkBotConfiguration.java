package ru.rassafel.foodsharing.vkbot.config;

import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.rassafel.foodsharing.vkbot.model.mapper.VkBotDtoMapper;

/**
 * @author rassafel
 */
@Configuration
@EnableConfigurationProperties(VkBotProperties.class)
@EnableScheduling
@AllArgsConstructor
@EntityScan(basePackages = "ru.rassafel.foodsharing.vkbot.model.domain")
public class VkBotConfiguration {
    public VkBotProperties properties;

    @Bean
    VkBotDtoMapper vkBotDtoMapper() {
        return VkBotDtoMapper.INSTANCE;
    }

    @Bean
    VkApiClient vkApiClient(VkBotProperties properties) {
        HttpTransportClient instance = new HttpTransportClient(
            properties.getClient().getRetryAttemptsNetworkErrorCount(),
            properties.getClient().getRetryAttemptsInvalidStatusCount());
        return new VkApiClient(instance,
            new GsonBuilder().disableHtmlEscaping().create(),
            properties.getClient().getRetryAttemptsInternalServerErrorCount());
    }

    @Bean
    public GroupActor groupActor(VkBotProperties properties) {
        return new GroupActor(properties.getGroupId(), properties.getAccessToken());
    }
}

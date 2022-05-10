package ru.rassafel.foodsharing.vkbot.config;

import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.AbstractQueryBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import ru.rassafel.foodsharing.vkbot.model.mapper.VkBotDtoMapper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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

    @Bean
    public BlockingQueue<AbstractQueryBuilder> vkQueryQueue(VkBotProperties properties) {
        return new ArrayBlockingQueue<>(properties.getClient().getMaxRequestQueueSize());
    }

    @Bean
    public TaskScheduler sendVkQueryTaskScheduler(VkBotProperties properties) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(properties.getClient().getMaxThreadCountForSendQueries());
        threadPoolTaskScheduler.setThreadNamePrefix("SendVkQueryTaskScheduler-");
        return threadPoolTaskScheduler;
    }
}

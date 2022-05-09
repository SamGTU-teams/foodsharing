package ru.rassafel.foodsharing.vkbot.config;

import com.vk.api.sdk.client.AbstractQueryBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Configuration
@ConfigurationProperties(prefix = "vk.api")
@EnableAsync
@Setter
@Getter
public class VkApiConfiguration {

    private int maxRequestQueueSize;
    private int maxThreadCountForSendQueries;
    private int maxTimeForSendSomeQueries;
    private int maxQueryCountPerTime;

    @Bean
    public BlockingQueue<AbstractQueryBuilder> vkQueryQueue() {
        return new ArrayBlockingQueue<>(maxRequestQueueSize);
    }

    @Bean
    public TaskScheduler sendVkQueryTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(maxThreadCountForSendQueries);
        threadPoolTaskScheduler.setThreadNamePrefix("SendVkQueryTaskScheduler-");
        return threadPoolTaskScheduler;
    }
}

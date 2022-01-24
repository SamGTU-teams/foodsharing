package ru.rassafel.foodsharing.vkparser.config;

import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rassafel
 */
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfiguration {
    @Bean
    VkApiClient vkApiClient(ApplicationProperties properties) {
        HttpTransportClient instance = new HttpTransportClient(
            properties.getRetryAttemptsNetworkErrorCount(),
            properties.getRetryAttemptsInvalidStatusCount());
        return new VkApiClient(instance,
            new GsonBuilder().disableHtmlEscaping().create(),
            properties.getRetryAttemptsInternalServerErrorCount());
    }
}

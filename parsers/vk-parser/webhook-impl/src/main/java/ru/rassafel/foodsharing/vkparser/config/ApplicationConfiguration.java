package ru.rassafel.foodsharing.vkparser.config;

import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rassafel.foodsharing.vkparser.model.entity.VkGroup;
import ru.rassafel.foodsharing.vkparser.model.mapper.GeoMapper;
import ru.rassafel.foodsharing.vkparser.model.mapper.RawPostMapper;
import ru.rassafel.foodsharing.vkparser.model.mapper.VkGroupMapper;

/**
 * @author rassafel
 */
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
@EntityScan(basePackageClasses = VkGroup.class)
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

    @Bean
    GeoMapper geoMapper() {
        return GeoMapper.INSTANCE;
    }

    @Bean
    RawPostMapper rawPostMapper() {
        return RawPostMapper.INSTANCE;
    }

    @Bean
    VkGroupMapper vkGroupMapper() {
        return VkGroupMapper.INSTANCE;
    }
}

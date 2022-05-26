package ru.rassafel.foodsharing.vkparser.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.apache.commons.lang3.tuple.MutablePair;
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
@EnableConfigurationProperties(VkParserProperties.class)
@EntityScan(basePackageClasses = VkGroup.class)
public class VkParserConfiguration {
    @Bean
    VkApiClient vkApiClient(VkParserProperties properties) {
        HttpTransportClient instance = new HttpTransportClient(
            properties.getClient().getRetryAttemptsNetworkErrorCount(),
            properties.getClient().getRetryAttemptsInvalidStatusCount());
        return new VkApiClient(instance,
            new GsonBuilder().disableHtmlEscaping().create(),
            properties.getClient().getRetryAttemptsInternalServerErrorCount());
    }

    @Bean
    public Cache<Long, MutablePair<Long, Boolean>> callbackCache(VkParserProperties properties) {
        return Caffeine.newBuilder()
            .expireAfterWrite(properties.getCallback().getCache().getExpirationTime())
            .build();
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

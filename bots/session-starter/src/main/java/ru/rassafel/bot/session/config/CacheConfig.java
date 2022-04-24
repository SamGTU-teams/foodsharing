package ru.rassafel.bot.session.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rassafel.bot.session.model.entity.place.Place;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Value("${cache.placeExpireDuration:10}")
    private Integer placeCacheToExpire;

    @Bean
    public Cache<Long, Place> geoPointCache() {
        return Caffeine.newBuilder()
            .expireAfterWrite(placeCacheToExpire, TimeUnit.MINUTES)
            .build();
    }
}

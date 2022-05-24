package ru.rassafel.foodsharing.session.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import ru.rassafel.foodsharing.session.model.entity.Place;

/**
 * @author rassafel
 */
@Data
@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "bot.session", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(SessionProperties.class)
//Скан всех компонентов модуля
@ComponentScan(basePackages = "ru.rassafel.bot.session")
@EnableJpaRepositories(basePackages = "ru.rassafel.bot.session.repository")
@EntityScan(basePackages = "ru.rassafel.bot.session.model.entity")
public class SessionAutoConfiguration {
    public SessionProperties properties;

    @Bean
    public MessageSource botMessagesSource() {
        ReloadableResourceBundleMessageSource messageSource =
            new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
            .build();
    }

    @Bean
    public Cache<Long, Place> geoPointCache(SessionProperties properties) {
        return Caffeine.newBuilder()
            .expireAfterWrite(properties.getPlacesCache().getExpirationTime())
            .build();
    }
}

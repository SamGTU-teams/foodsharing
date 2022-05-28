package ru.rassafel.foodsharing.session.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;
import ru.rassafel.foodsharing.session.model.dto.SessionResponse;
import ru.rassafel.foodsharing.session.model.entity.Place;
import ru.rassafel.foodsharing.session.repository.CallbackLockRepository;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.Objects.nonNull;

/**
 * @author rassafel
 */
@Data
@RequiredArgsConstructor
@Configuration("sessionConfig")
@ConditionalOnProperty(prefix = "bot.session", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(SessionProperties.class)
//Скан всех компонентов модуля
@ComponentScan(basePackages = "ru.rassafel.foodsharing.session")
@EnableJpaRepositories(basePackages = "ru.rassafel.foodsharing.session.repository")
@EntityScan(basePackages = "ru.rassafel.foodsharing.session.model.entity")
public class SessionAutoConfiguration {
    public final SessionProperties properties;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
            .build();
    }

    @Bean
    public Cache<Long, Place> geoPointCache(SessionProperties properties) {
        return Caffeine.newBuilder()
            .expireAfterWrite(properties.getPlace().getCache().getExpirationTime())
            .build();
    }

    @Bean
    public BlockingQueue<SessionResponse> queryQueue() {
        return new ArrayBlockingQueue<>(properties.getMessenger().getMaxRequestQueueSize());
    }

    @Bean
    public TaskScheduler sendQueryTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(properties.getMessenger().getMaxThreadCountForSendQueries());
        threadPoolTaskScheduler.setThreadNamePrefix("SendQueryTaskScheduler-");
        return threadPoolTaskScheduler;
    }

    @Configuration
    @ConditionalOnMissingBean(CallbackLockRepository.class)
    protected static class DefaultCallbackLockRepositoryConfiguration {
        @Bean
        public Cache<Long, MutablePair<Long, Boolean>> callbackCache(SessionProperties properties) {
            return Caffeine.newBuilder()
                .expireAfterWrite(properties.getCallback().getCache().getExpirationTime())
                .build();
        }

        @Bean
        public DefaultCallbackLockRepository defaultCallbackLockRepository(Cache<Long, MutablePair<Long, Boolean>> callbackCache) {
            return new DefaultCallbackLockRepository(callbackCache);
        }

        @RequiredArgsConstructor
        public static class DefaultCallbackLockRepository implements CallbackLockRepository {
            private final Cache<Long, MutablePair<Long, Boolean>> callbackCache;

            // ToDo: synchronise method
            @Override
            public boolean lock(Long userId, Long messageId) {
                MutablePair<Long, Boolean> lock = getLock(userId);
                if (nonNull(lock)) {
                    if (lock.getLeft() < messageId) {
                        if (lock.getRight()) {
                            // Новое сообщение, обработчик завершил
                            callbackCache.put(userId, MutablePair.of(messageId, false));
                            return true;
                        } else {
                            // Новое сообщение, обработчик не завершил
                            lock.setLeft(messageId);
                            return false;
                        }
                    } else {
                        // Старое сообщение
                        return false;
                    }
                }
                // Нету сообщений в кэше
                callbackCache.put(userId, MutablePair.of(messageId, false));
                return true;
            }

            @Override
            public void unlock(Long userId) {
                MutablePair<Long, Boolean> lock = getLock(userId);
                if (nonNull(lock)) {
                    lock.setRight(true);
                }
            }

            public MutablePair<Long, Boolean> getLock(Long userId) {
                return callbackCache.getIfPresent(userId);
            }
        }
    }
}

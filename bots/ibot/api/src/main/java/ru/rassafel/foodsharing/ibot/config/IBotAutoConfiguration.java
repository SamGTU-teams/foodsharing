package ru.rassafel.foodsharing.ibot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author rassafel
 */
@Configuration
@EnableFeignClients
@EnableConfigurationProperties(IBotProperties.class)
public class IBotAutoConfiguration {
}

package ru.rassafel.foodsharing.ibot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author rassafel
 */
@Configuration
@EnableConfigurationProperties(IBotProperties.class)
public class IBotAutoConfiguration {
}

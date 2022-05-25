package ru.rassafel.foodsharing.ibot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import ru.rassafel.foodsharing.ibot.controller.IBotController;

/**
 * @author rassafel
 */
@Configuration
@EnableFeignClients(basePackageClasses = IBotController.class)
@ConditionalOnProperty(name = {"feign.ibot.url"})
@EnableConfigurationProperties(IBotProperties.class)
public class IBotAutoConfiguration {
}

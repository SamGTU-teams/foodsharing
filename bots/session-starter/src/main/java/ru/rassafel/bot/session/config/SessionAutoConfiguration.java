package ru.rassafel.bot.session.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author rassafel
 */
@Configuration
@EnableConfigurationProperties(SessionProperties.class)
public class SessionAutoConfiguration {
}

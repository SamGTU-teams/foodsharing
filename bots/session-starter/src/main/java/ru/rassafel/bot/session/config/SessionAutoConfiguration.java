package ru.rassafel.bot.session.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author rassafel
 */
@Configuration
@ConditionalOnProperty(name = "bot.session.enabled", matchIfMissing = true)
@EnableConfigurationProperties(SessionProperties.class)
//Скан всех компонентов модуля
@ComponentScan(basePackages = "ru.rassafel.bot.session")
public class SessionAutoConfiguration {
}

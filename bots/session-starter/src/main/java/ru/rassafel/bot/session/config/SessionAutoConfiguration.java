package ru.rassafel.bot.session.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author rassafel
 */
@Configuration
@ConditionalOnProperty(name = "bot.session.enabled", matchIfMissing = true)
@EnableConfigurationProperties(SessionProperties.class)
//Скан всех компонентов модуля
@ComponentScan(basePackages = "ru.rassafel.bot.session")
@EnableJpaRepositories(basePackages = "ru.rassafel.bot.session.repository")
@EntityScan(basePackages = "ru.rassafel.bot.session.model.entity")
public class SessionAutoConfiguration {
}

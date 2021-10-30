package ru.rassafel.ibot.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author rassafel
 */
@Configuration
@ConditionalOnProperty(name = {"feign.ibot.url"})
public class IBotAutoConfiguration {
}

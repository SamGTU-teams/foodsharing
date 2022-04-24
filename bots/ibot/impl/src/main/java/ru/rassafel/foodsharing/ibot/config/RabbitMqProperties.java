package ru.rassafel.foodsharing.ibot.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@Data
@ConfigurationProperties(prefix = RabbitMqProperties.PREFIX)
public class RabbitMqProperties {
    public static final String PREFIX = "spring.rabbitmq";

    private final Consumer consumer = new Consumer();

    @Getter
    @Setter
    public static class Consumer {
        private String exchange = "post-raw-default-exchange";
        private String queue = "post.raw.analyzer.default";
    }
}

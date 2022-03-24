package ru.rassafel.foodsharing.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@ConfigurationProperties(prefix = RabbitMqProperties.PREFIX)
@Getter
@Setter
public class RabbitMqProperties {
    public static final String PREFIX = "spring.rabbitmq";

    private Consumer consumer;
    private Producer producer;

    @Getter
    @Setter
    public class Consumer {
        private String exchange = "post-raw-exchange";
        private String queue = "post.raw.analyzer";
    }

    @Getter
    @Setter
    public class Producer {
        private String exchange = "post-ready-exchange";
    }
}

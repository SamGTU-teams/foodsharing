package ru.rassafel.foodsharing.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author rassafel
 */
@ConfigurationProperties(prefix = RabbitMqProperties.PREFIX)
@Getter
public class RabbitMqProperties {
    public static final String PREFIX = "spring.rabbitmq";

    private final Consumer consumer = new Consumer();
    private final Producer producer = new Producer();

    @Getter
    @Setter
    public class Consumer {
        private String exchange = "post-raw-default-exchange";
        private String queue = "post.raw.analyzer.default";
    }

    @Getter
    @Setter
    public class Producer {
        private String exchange = "post-ready-default-exchange";
    }
}

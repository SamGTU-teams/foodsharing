package ru.rassafel.foodsharing.ad.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rassafel
 */
@Configuration
@EnableConfigurationProperties(RabbitMqProperties.class)
public class RabbitMqConfiguration {
    @Bean
    MessageConverter jsonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                  MessageConverter jsonMessageConverter,
                                  RabbitMqProperties properties) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setExchange(properties.getAdPost().getExchange());
        return rabbitTemplate;
    }

    @Bean
    FanoutExchange adExchange(RabbitMqProperties properties) {
        return ExchangeBuilder.fanoutExchange(properties.getAdPost().getExchange())
            .build();
    }

    @Bean
    Queue adStorageQueue(RabbitMqProperties properties) {
        return QueueBuilder.durable(properties.getAdPost().getStorage())
            .build();
    }

    @Bean
    Binding adStorageBinding(RabbitMqProperties properties) {
        return BindingBuilder.bind(adStorageQueue(properties))
            .to(adExchange(properties));
    }
}

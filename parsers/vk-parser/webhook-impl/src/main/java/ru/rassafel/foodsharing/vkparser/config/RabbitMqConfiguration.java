package ru.rassafel.foodsharing.vkparser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
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
@EnableRabbit
@EnableConfigurationProperties(RabbitMqProperties.class)
public class RabbitMqConfiguration {
    @Bean
    FanoutExchange fanoutExchange(RabbitMqProperties properties) {
        return ExchangeBuilder.fanoutExchange(properties.getRawPost().getExchange())
            .durable(true)
            .build();
    }

    @Bean
    Queue storageQueue(RabbitMqProperties properties) {
        return QueueBuilder.durable(properties.getRawPost().getStorage())
            .build();
    }

    @Bean
    Binding bindingMessages(RabbitMqProperties properties) {
        return BindingBuilder
            .bind(storageQueue(properties))
            .to(fanoutExchange(properties));
    }

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
        rabbitTemplate.setExchange(properties.getRawPost().getExchange());
        return rabbitTemplate;
    }
}

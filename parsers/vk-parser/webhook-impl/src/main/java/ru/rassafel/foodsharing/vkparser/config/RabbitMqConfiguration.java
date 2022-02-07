package ru.rassafel.foodsharing.vkparser.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rassafel
 */
@EnableRabbit
@Configuration
@ConditionalOnProperty(prefix = RabbitMqProperties.PREFIX, value = "exchangeName")
@EnableConfigurationProperties(RabbitMqProperties.class)
public class RabbitMqConfiguration {
    @Bean
    FanoutExchange fanoutExchange(RabbitMqProperties properties) {
        return new FanoutExchange(properties.getExchangeName());
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                  MessageConverter jsonMessageConverter,
                                  RabbitMqProperties properties) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setExchange(properties.getExchangeName());
        return rabbitTemplate;
    }
}

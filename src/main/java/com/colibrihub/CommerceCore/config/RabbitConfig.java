package com.colibrihub.CommerceCore.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app.rabbitmq.routing-key}")
    private String ROUTING_KEY;
    @Value("${app.rabbitmq.queue}")
    private String QUEUE_NAME;
    @Value("${app.rabbitmq.exchange}")
    private String EXCHANGE_NAME;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange ordersExchange() {
        return new  DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue notificationsQueue() {
        return QueueBuilder
                .durable(QUEUE_NAME)
                .build();
    }

    @Bean
    public Binding notificationsBinding() {
        return BindingBuilder
                .bind(notificationsQueue())
                .to(ordersExchange())
                .with(ROUTING_KEY);
    }
}

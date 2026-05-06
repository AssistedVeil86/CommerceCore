package com.colibrihub.CommerceCore.service.Impl;

import com.colibrihub.CommerceCore.event.OrderConfirmedEvent;
import com.colibrihub.CommerceCore.service.OrderEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderEventPublisherImpl implements OrderEventPublisher {

    @Value("${app.rabbitmq.routing-key}")
    private String ROUTING_KEY;
    @Value("${app.rabbitmq.exchange}")
    private String EXCHANGE_NAME;

    private final RabbitTemplate rabbitTemplate;

    public OrderEventPublisherImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishConfirmedOrder(OrderConfirmedEvent event) {
        log.info("Order confirmed event received: {}", event);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event);
        log.info("Order confirmed event sent: {}", event);
    }
}

package com.colibrihub.CommerceCore.service;

import com.colibrihub.CommerceCore.event.OrderConfirmedEvent;

public interface OrderEventPublisher {
    void publishConfirmedOrder(OrderConfirmedEvent event);
}

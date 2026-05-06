package com.colibrihub.CommerceCore.event;

import com.colibrihub.CommerceCore.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderConfirmedEvent {

    private Long orderId;
    private Long clientId;
    private String clientEmail;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<ProductItemEvent> products;

    public OrderConfirmedEvent(){
        this.createdAt = LocalDateTime.now();
    }

    public OrderConfirmedEvent(Order order, List<ProductItemEvent> products){
        this.orderId = order.getId();
        this.clientId = order.getClientId();
        this.clientEmail = order.getClientEmail();
        this.total = order.getTotal();
        this.createdAt = order.getCreatedAt();
        this.products = products;
    }
}

package com.colibrihub.CommerceCore.mapper;

import com.colibrihub.CommerceCore.dto.Request.OrderRequest;
import com.colibrihub.CommerceCore.dto.Response.OrderResponse;
import com.colibrihub.CommerceCore.entity.Order;
import com.colibrihub.CommerceCore.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    public OrderResponse toResponse(Order order) {

        var orderItems = order.getOrderItems()
                .stream()
                .map(orderItemMapper::toResponse)
                .toList();

        var response = new OrderResponse();

        response.setId(order.getId());
        response.setClientEmail(order.getClientEmail());
        response.setClientId(order.getClientId());
        response.setOrderStatus(order.getOrderStatus());
        response.setTotal(order.getTotal());
        response.setDatePaid(order.getDatePaid());
        response.setOrderItems(orderItems);

        return response;
    }

    public Order toEntity(OrderRequest req, BigDecimal total) {
        var entity = new Order();

        entity.setClientId(req.getClientId());
        entity.setClientEmail(req.getClientEmail());
        entity.setOrderStatus(OrderStatus.PENDING);
        entity.setTotal(total);

        return entity;
    }
}

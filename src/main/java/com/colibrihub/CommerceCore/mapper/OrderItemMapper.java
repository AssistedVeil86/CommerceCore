package com.colibrihub.CommerceCore.mapper;

import com.colibrihub.CommerceCore.dto.Request.OrderItemRequest;
import com.colibrihub.CommerceCore.dto.Response.OrderItemResponse;
import com.colibrihub.CommerceCore.entity.OrderItem;
import com.colibrihub.CommerceCore.entity.Product;
import com.colibrihub.CommerceCore.event.ProductItemEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderItemMapper {

    public ProductItemEvent toProductItemEvent(OrderItem orderItem) {
        var productItemEvent = new ProductItemEvent();

        productItemEvent.setName(orderItem.getProduct().getName());
        productItemEvent.setQuantity(orderItem.getQuantity());
        productItemEvent.setSku(orderItem.getProduct().getSku());
        productItemEvent.setPrice(orderItem.getUnitPrice());

        return productItemEvent;
    }

    public OrderItem toEntity(OrderItemRequest req, Product product) {
        var entity = new OrderItem();

        entity.setQuantity(req.getQuantity());
        entity.setUnitPrice(product.getRegularPrice());
        entity.setProduct(product);
        entity.setSubtotal(calculateSubTotal(req.getQuantity(), product.getRegularPrice()));

        return entity;
    }

    public OrderItemResponse toResponse(OrderItem orderItem) {
        var response = new OrderItemResponse();

        response.setId(orderItem.getId());
        response.setQuantity(orderItem.getQuantity());
        response.setSubtotal(orderItem.getSubtotal());
        response.setUnitPrice(orderItem.getUnitPrice());
        response.setProductName(orderItem.getProduct().getName());
        response.setProductSku(orderItem.getProduct().getSku());

        return response;
    }

    private BigDecimal calculateSubTotal(int quantity, BigDecimal unitPrice) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

package com.colibrihub.CommerceCore.mapper;

import com.colibrihub.CommerceCore.dto.Response.InvoiceItemResponse;
import com.colibrihub.CommerceCore.entity.InvoiceItem;
import com.colibrihub.CommerceCore.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class InvoiceItemMapper {

    public InvoiceItem toEntity(OrderItem orderItem, String productName) {
        var entity = new InvoiceItem();

        entity.setProductName(productName);
        entity.setQuantity(orderItem.getQuantity());
        entity.setUnitPrice(orderItem.getUnitPrice());
        entity.setUnitSubtotal(orderItem.getSubtotal());

        return entity;
    }

    public InvoiceItemResponse toResponse(InvoiceItem invoiceItem) {
        var response = new InvoiceItemResponse();

        response.setId(invoiceItem.getId());
        response.setProductName(invoiceItem.getProductName());
        response.setQuantity(invoiceItem.getQuantity());
        response.setUnitPrice(invoiceItem.getUnitPrice());
        response.setUnitSubtotal(invoiceItem.getUnitSubtotal());

        return response;
    }
}

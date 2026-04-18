package com.colibrihub.CommerceCore.mapper;

import com.colibrihub.CommerceCore.dto.Response.InvoiceItemResponse;
import com.colibrihub.CommerceCore.dto.Response.InvoiceResponse;
import com.colibrihub.CommerceCore.entity.Invoice;
import com.colibrihub.CommerceCore.entity.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Component
public class InvoiceMapper {

    private final InvoiceItemMapper invoiceItemMapper;

    public InvoiceMapper(InvoiceItemMapper invoiceItemMapper) {
        this.invoiceItemMapper = invoiceItemMapper;
    }

    public InvoiceResponse toResponse(Invoice invoice) {
        var response = new InvoiceResponse();

        var invoiceItems = invoice.getItems().stream()
                .map(invoiceItemMapper::toResponse)
                .toList();

        response.setId(invoice.getId());
        response.setItems(invoiceItems);
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        response.setClientEmail(invoice.getClientEmail());
        response.setIva(invoice.getIva());
        response.setTotal(invoice.getTotal());
        response.setSubtotal(invoice.getSubTotal());

        return response;
    }

    public Invoice toEntity(Order order) {
        var entity =  new Invoice();

        var subtotal = getOrderSubtotal(order.getTotal());
        var iva = getOrderIva(subtotal);

        entity.setClientEmail(order.getClientEmail());
        entity.setInvoiceNumber(UUID.randomUUID().toString());
        entity.setTotal(order.getTotal());
        entity.setSubTotal(subtotal);
        entity.setIva(iva);

        return entity;
    }

    private BigDecimal getOrderSubtotal(BigDecimal total) {
        return total.divide(BigDecimal.valueOf(1.13), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal getOrderIva (BigDecimal subtotal) {
        return subtotal.multiply(BigDecimal.valueOf(0.13));
    }
}

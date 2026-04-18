package com.colibrihub.CommerceCore.dto.Response;

import com.colibrihub.CommerceCore.entity.InvoiceItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InvoiceResponse {
    private Long id;
    private String clientEmail;
    private String invoiceNumber;
    private BigDecimal subtotal;
    private BigDecimal total;
    private BigDecimal iva;
    private List<InvoiceItemResponse> items;
}

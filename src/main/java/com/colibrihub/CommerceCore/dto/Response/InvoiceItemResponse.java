package com.colibrihub.CommerceCore.dto.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItemResponse {
    private Long id;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal unitSubtotal;
}

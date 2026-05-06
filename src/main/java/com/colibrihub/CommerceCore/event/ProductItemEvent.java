package com.colibrihub.CommerceCore.event;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductItemEvent {
    private String name;
    private String sku;
    private BigDecimal price;
    private int quantity;
}

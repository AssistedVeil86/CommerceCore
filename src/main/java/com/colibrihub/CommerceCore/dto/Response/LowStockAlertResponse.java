package com.colibrihub.CommerceCore.dto.Response;

import lombok.Data;

@Data
public class LowStockAlertResponse {
    private Long productId;
    private String productName;
    private String sku;
    private int stockQuantity;
}

package com.colibrihub.CommerceCore.service;

import com.colibrihub.CommerceCore.dto.Response.LowStockAlertResponse;

import java.util.List;

public interface StockAlertsService {
    List<LowStockAlertResponse> getProductsByLowStock();
}

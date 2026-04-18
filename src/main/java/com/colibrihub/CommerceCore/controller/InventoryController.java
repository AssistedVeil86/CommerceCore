package com.colibrihub.CommerceCore.controller;

import com.colibrihub.CommerceCore.dto.Response.ApiResponse;
import com.colibrihub.CommerceCore.dto.Response.LowStockAlertResponse;
import com.colibrihub.CommerceCore.service.StockAlertsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/inventory")
public class InventoryController {

    private final StockAlertsService stockAlertsService;

    public InventoryController(StockAlertsService stockAlertsService) {
        this.stockAlertsService = stockAlertsService;
    }

    @GetMapping("alerts")
    public ResponseEntity<ApiResponse<List<LowStockAlertResponse>>> getAllProducts() {
        var response = stockAlertsService.getProductsByLowStock();
        return ResponseEntity.ok(ApiResponse.success("Products with Low Stock retrieved!", response));
    }
}

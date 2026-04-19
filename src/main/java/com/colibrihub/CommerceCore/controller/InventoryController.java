package com.colibrihub.CommerceCore.controller;

import com.colibrihub.CommerceCore.dto.Response.ApiResponse;
import com.colibrihub.CommerceCore.dto.Response.LowStockAlertResponse;
import com.colibrihub.CommerceCore.service.StockAlertsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/inventory")
@Tag(name = "Inventory", description = "Gestión de inventario y alertas de stock bajo")
public class InventoryController {

    private final StockAlertsService stockAlertsService;

    public InventoryController(StockAlertsService stockAlertsService) {
        this.stockAlertsService = stockAlertsService;
    }

    @GetMapping("alerts")
    @Operation(
            summary = "Obtener productos con poco stock",
            description = "Devuelve una lista de productos con stock se encuentra por debajo del minimo permitido"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Lista de productos con bajo stock obtenida de forma correcta"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",
                    description = "Error interno del servidor", content = {
                    @Content(mediaType = "application/problem+json") })
    })
    public ResponseEntity<ApiResponse<List<LowStockAlertResponse>>> getAllProducts() {
        var response = stockAlertsService.getProductsByLowStock();
        return ResponseEntity.ok(ApiResponse.success("Products with Low Stock retrieved!", response));
    }
}

package com.colibrihub.CommerceCore.dto.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Detalle de un producto dentro de una orden")
public class OrderItemResponse {
    @Schema(description = "ID del item de la orden", example = "1")
    private Long id;
    @Schema(description = "Cantidad del producto", example = "2")
    private int quantity;
    @Schema(description = "Precio unitario del producto", example = "10.50")
    private BigDecimal unitPrice;
    @Schema(description = "Subtotal calculado (cantidad * precio)", example = "21.00")
    private BigDecimal subtotal;
    @Schema(description = "Nombre del producto", example = "laptop gamer")
    private String productName;
    @Schema(description = "SKU del producto", example = "LAP-123")
    private String productSku;
}

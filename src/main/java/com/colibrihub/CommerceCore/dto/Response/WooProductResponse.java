package com.colibrihub.CommerceCore.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Producto obtenido desde WooCommerce")
public class WooProductResponse {
    @Schema(description = "Nombre del producto", example = "Tablet")
    private String name;
    @Schema(description = "Descripción completa", example = "Tablet de 11 pulgadas")
    private String description;
    @JsonProperty("short_description")
    @Schema(description = "Descripción corta", example = "Tablet Xiaomi de Color Azul")
    private String shortDescription;
    @Schema(description = "SKU del producto", example = "XIA-123")
    private String sku;
    @Schema(description = "Estado del producto en WooCommerce", example = "publish")
    private String status;
    @JsonProperty("regular_price")
    @Schema(description = "Precio regular", example = "500")
    private String regularPrice;
    @JsonProperty("stock_quantity")
    @Schema(description = "Cantidad en stock", example = "935")
    private int stockQuantity;
}

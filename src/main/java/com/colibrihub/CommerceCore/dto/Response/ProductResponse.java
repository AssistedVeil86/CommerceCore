package com.colibrihub.CommerceCore.dto.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta que representa un producto")
public class ProductResponse {
    @Schema(description = "ID del producto", example = "1")
    private Long id;
    @Schema(description = "Nombre del producto", example = "Samsung A10")
    private String name;
    @Schema(description = "Descripción completa del producto", example = "Samsung A10 de Color Azul")
    private String description;
    @Schema(description = "Descripción corta del producto", example = "Samsung A10 for Kids")
    private String shortDescription;
    @Schema(description = "SKU del producto", example = "IHP-123")
    private String sku;
    @Schema(description = "Estado del producto", example = "publish")
    private String status;
    @Schema(description = "Precio regular", example = "700")
    private BigDecimal regularPrice;
    @Schema(description = "Cantidad en stock", example = "40")
    private int stockQuantity;
}

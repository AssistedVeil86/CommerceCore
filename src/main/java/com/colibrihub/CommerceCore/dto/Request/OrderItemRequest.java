package com.colibrihub.CommerceCore.dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.sql.Update;

@Data
@Schema(description = "Item de una orden")
public class OrderItemRequest {
    @Schema(description = "ID del producto", example = "1")
    @NotNull(message = "Product Id is required")
    private Long productId;
    @Schema(description = "Cantidad del producto", example = "2")
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Minimum quantity must be greater than 0")
    private int quantity;
}

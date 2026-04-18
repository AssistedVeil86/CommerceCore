package com.colibrihub.CommerceCore.dto.Request;

import com.colibrihub.CommerceCore.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request para actualizar el estado de una orden")
public class OrderStatusRequest {
    @Schema(description = "Nuevo estado de la orden", example = "COMPLETED")
    @NotNull(message = "Order Status is required")
    private OrderStatus orderStatus;
}

package com.colibrihub.CommerceCore.dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Request para crear una orden")
public class OrderRequest {
    @Schema(description = "ID del cliente", example = "10")
    @NotNull(message = "ClientId is required")
    private Long clientId;
    @Email
    @NotEmpty(message = "Client Email is mandatory")
    @Schema(description = "Correo del cliente", example = "cliente@email.com")
    private String clientEmail;
    @NotNull(message = "Order Items are mandatory")
    @Schema(description = "Lista de productos en la orden")
    private List<OrderItemRequest> items;
}

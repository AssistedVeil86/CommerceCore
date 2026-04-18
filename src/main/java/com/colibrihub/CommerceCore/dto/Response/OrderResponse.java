package com.colibrihub.CommerceCore.dto.Response;

import com.colibrihub.CommerceCore.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Respuesta que representa una orden completa")
public class OrderResponse {

    @Schema(description = "ID de la orden", example = "1")
    private Long id;
    @Schema(description = "ID del cliente", example = "10")
    private Long clientId;
    @Schema(description = "Correo del cliente", example = "cliente@email.com")
    private String clientEmail;
    @Schema(description = "Estado de la orden", example = "CONFIRMED")
    private OrderStatus orderStatus;
    @Schema(description = "Fecha de pago", example = "2024-05-20T10:15:30")
    private LocalDateTime datePaid;
    @Schema(description = "Total de la orden", example = "150.75")
    private BigDecimal total;
    @Schema(description = "Lista de productos dentro de la orden")
    private List<OrderItemResponse> orderItems;
}

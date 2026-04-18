package com.colibrihub.CommerceCore.controller;

import com.colibrihub.CommerceCore.dto.Request.OrderRequest;
import com.colibrihub.CommerceCore.dto.Response.ApiResponse;
import com.colibrihub.CommerceCore.dto.Response.OrderResponse;
import com.colibrihub.CommerceCore.dto.Request.OrderStatusRequest;
import com.colibrihub.CommerceCore.dto.Response.PagedResponse;
import com.colibrihub.CommerceCore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/orders")
@Tag(name = "Orders", description = "Gestión de órdenes")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Obtener órdenes por cliente",
            description = "Obtener las órdenes del cliente con paginación de 20 órdenes por página")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Órdenes obtenidas"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Parámetros inválidos", content = {
                    @Content(mediaType = "application/problem+json")
            })
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<OrderResponse>>> findByClientId(
            @Parameter(description = "Número de página", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Cantidad de Ordenes", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "ID del cliente", example = "1")
            @RequestParam Long clientId) {

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(size, 100));
        var orders = orderService.findByClientId(pageable, clientId);

        return ResponseEntity.ok(ApiResponse.success("Orders Retrieved!", orders));
    }

    @PutMapping("{id}/status")
    @Operation(
            summary = "Actualizar estado de una orden",
            description = "Actualizar el estado de una orden a CANCELLED, CONFIRMED,PENDING, SHIPPED, o DELIVERED")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Estado de Orden Actualizado Exitosamente!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "Orden del Cliente no encontrado", content = {
                            @Content(mediaType = "application/problem+json")
            }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Operación inválida con las órdenes", content = {
                            @Content(mediaType = "application/problem+json")
            }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "422", description = "Cambio de estado inválido — no se puede modificar una orden DELIVERED, SHIPPED o CANCELLED", content = {
                            @Content(mediaType = "application/problem+json")
            }),
    })
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @Valid @RequestBody OrderStatusRequest req,
            @Parameter(description = "ID de la orden a actualizar", example = "1")
            @PathVariable Long id) {
        var response = orderService.updateStatus(id, req);
        return ResponseEntity.ok(ApiResponse.success("Order Status Updated!", response));
    }

    @PostMapping
    @Operation(
            summary = "Crear una nueva orden",
            description = "Crear una orden por cliente junto a sus productos deseados")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201", description = "Orden Creada Exitosamente!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "422", description = "Datos de Entrada Válidos", content = {
                    @Content(mediaType = "application/problem+json"),
            }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "Producto deseado de la orden no encontrado", content = {
                    @Content(mediaType = "application/problem+json")
            })
    })
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderRequest req) throws URISyntaxException {
        var response = orderService.createOrder(req);
        return ResponseEntity.created(new URI("api/orders/" + response.getId()))
                .body(ApiResponse.success("Order Created!", response));
    }
}
package com.colibrihub.CommerceCore.controller;

import com.colibrihub.CommerceCore.dto.Response.ApiResponse;
import com.colibrihub.CommerceCore.dto.Response.InvoiceResponse;
import com.colibrihub.CommerceCore.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/invoices")
@Tag(name = "Invoices", description = "Gestión de facturas")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("{orderId}")
    @Operation(
            summary = "Obtener factura por Id de la orden",
            description = "Devuelve la factura asociada a una sola orden especifica"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Factura encontrada para Orden Solicitada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = "Factura no encontrada para Orden Solicitada", content = {
                    @Content(mediaType = "application/problem+json")})
    })
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoice(
            @Parameter(description = "ID de la orden", example = "1")
            @PathVariable Long orderId) {
        var response = invoiceService.getInvoiceByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success(
                "Invoice for Order with Id " + orderId + " found!", response));
    }
}

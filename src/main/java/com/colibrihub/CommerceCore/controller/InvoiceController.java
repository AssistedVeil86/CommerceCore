package com.colibrihub.CommerceCore.controller;

import com.colibrihub.CommerceCore.dto.Response.ApiResponse;
import com.colibrihub.CommerceCore.dto.Response.InvoiceResponse;
import com.colibrihub.CommerceCore.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("{orderId}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoice(@PathVariable Long orderId) {
        var response = invoiceService.getInvoiceByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success(
                "Invoice for Order with Id " + orderId + " found!", response));
    }
}

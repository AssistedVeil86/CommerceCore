package com.colibrihub.CommerceCore.service;

import com.colibrihub.CommerceCore.dto.Response.InvoiceResponse;
import com.colibrihub.CommerceCore.entity.Invoice;
import com.colibrihub.CommerceCore.entity.Order;

public interface InvoiceService {
    InvoiceResponse createInvoice(Order order);
    InvoiceResponse getInvoiceByOrderId(Long orderId);
}

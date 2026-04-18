package com.colibrihub.CommerceCore.service.Impl;

import com.colibrihub.CommerceCore.Exception.ResourceNotFoundException;
import com.colibrihub.CommerceCore.dto.Response.InvoiceResponse;
import com.colibrihub.CommerceCore.entity.InvoiceItem;
import com.colibrihub.CommerceCore.entity.Order;
import com.colibrihub.CommerceCore.mapper.InvoiceItemMapper;
import com.colibrihub.CommerceCore.mapper.InvoiceMapper;
import com.colibrihub.CommerceCore.repository.InvoiceRepository;
import com.colibrihub.CommerceCore.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceItemMapper invoiceItemMapper;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            InvoiceMapper invoiceMapper,
            InvoiceItemMapper invoiceItemMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.invoiceItemMapper = invoiceItemMapper;
    }

    @Override
    public InvoiceResponse createInvoice(Order order) {

        var invoiceItems = new ArrayList<InvoiceItem>();

        order.getOrderItems().forEach(item -> {
            var invoiceItem = invoiceItemMapper.toEntity(item, item.getProduct().getName());
            invoiceItems.add(invoiceItem);
        });

        var invoice = invoiceMapper.toEntity(order);

        invoiceItems.forEach(invoiceItem -> invoiceItem.setInvoice(invoice));

        invoice.setOrder(order);
        invoice.setItems(invoiceItems);

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    @Override
    public InvoiceResponse getInvoiceByOrderId(Long orderId) {
        var invoice = invoiceRepository.findByOrderId(orderId);

        if (invoice == null)
            throw new ResourceNotFoundException("Invoice for Order with Id " + orderId + " not found");

        return invoiceMapper.toResponse(invoice);
    }
}
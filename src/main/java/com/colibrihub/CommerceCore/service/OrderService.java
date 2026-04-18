package com.colibrihub.CommerceCore.service;

import com.colibrihub.CommerceCore.dto.Request.OrderRequest;
import com.colibrihub.CommerceCore.dto.Response.OrderResponse;
import com.colibrihub.CommerceCore.dto.Request.OrderStatusRequest;
import com.colibrihub.CommerceCore.dto.Response.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    PagedResponse<OrderResponse> findByClientId(Pageable pageable, Long clientId);
    OrderResponse updateStatus(Long id, OrderStatusRequest req);
    OrderResponse createOrder(OrderRequest req);
}

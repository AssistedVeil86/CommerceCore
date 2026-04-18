package com.colibrihub.CommerceCore.service.Impl;

import com.colibrihub.CommerceCore.dto.Response.LowStockAlertResponse;
import com.colibrihub.CommerceCore.mapper.ProductMapper;
import com.colibrihub.CommerceCore.repository.ProductRepository;
import com.colibrihub.CommerceCore.service.StockAlertsService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockAlertsServiceImpl implements StockAlertsService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public StockAlertsServiceImpl(
            ProductRepository productRepository,
            ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Cacheable(value = "lowStockAlerts", key = "'low-stock-products'")
    public List<LowStockAlertResponse> getProductsByLowStock() {
        return productRepository.findByStockQuantityLessThan(5)
                .stream().map(productMapper::mapToLowAlertResponse).toList();
    }
}

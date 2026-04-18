package com.colibrihub.CommerceCore.mapper;

import com.colibrihub.CommerceCore.dto.Response.LowStockAlertResponse;
import com.colibrihub.CommerceCore.dto.Response.ProductResponse;
import com.colibrihub.CommerceCore.dto.Response.WooProductResponse;
import com.colibrihub.CommerceCore.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        var response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setSku(product.getSku());
        response.setStatus(product.getStatus());
        response.setRegularPrice(product.getRegularPrice());
        response.setDescription(product.getDescription());
        response.setShortDescription(product.getShortDescription());
        response.setStockQuantity(product.getStockQuantity());

        return response;
    }

    public Product updateEntity(Product entity, WooProductResponse response) {

        entity.setName(response.getName());
        entity.setSku(response.getSku());
        entity.setRegularPrice(parsePrice(response.getRegularPrice()));
        entity.setStatus(response.getStatus());
        entity.setDescription(response.getDescription());
        entity.setShortDescription(response.getShortDescription());
        entity.setStockQuantity(response.getStockQuantity());

        return entity;
    }

    public LowStockAlertResponse mapToLowAlertResponse(Product product) {
        var response = new LowStockAlertResponse();

        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setSku(product.getSku());
        response.setStockQuantity(product.getStockQuantity());

        return response;
    }

    private BigDecimal parsePrice(String price) {
        if (price == null || price.isBlank()) return BigDecimal.ZERO;
        return new BigDecimal(price);
    }
}

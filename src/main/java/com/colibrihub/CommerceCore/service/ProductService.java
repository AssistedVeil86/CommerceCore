package com.colibrihub.CommerceCore.service;

import com.colibrihub.CommerceCore.dto.Response.PagedResponse;
import com.colibrihub.CommerceCore.dto.Response.ProductResponse;
import com.colibrihub.CommerceCore.entity.Product;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    PagedResponse<ProductResponse> getProducts(Pageable pageable);
    ProductResponse getProductById(Long id);
    void subtractQuantityFromStock(Product product, int quantity);
    void addQuantityToStock(Product product, int quantity);
}

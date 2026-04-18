package com.colibrihub.CommerceCore.service.Impl;

import com.colibrihub.CommerceCore.Exception.ResourceNotFoundException;
import com.colibrihub.CommerceCore.dto.Response.PagedResponse;
import com.colibrihub.CommerceCore.dto.Response.ProductResponse;
import com.colibrihub.CommerceCore.entity.Product;
import com.colibrihub.CommerceCore.mapper.ProductMapper;
import com.colibrihub.CommerceCore.repository.ProductRepository;
import com.colibrihub.CommerceCore.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public ProductServiceImpl(
            ProductMapper productMapper,
            ProductRepository productRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    @Override
    @Cacheable(value = "pagedProducts", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public PagedResponse<ProductResponse> getProducts(Pageable pageable) {
        var products =  productRepository.findAll(pageable).map(productMapper::toResponse);
        return PagedResponse.of(products);
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public ProductResponse getProductById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "pagedProducts", "lowStockAlerts"}, allEntries = true)
    public void subtractQuantityFromStock(Product product, int quantity) {
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "pagedProducts", "lowStockAlerts"}, allEntries = true)
    public void addQuantityToStock(Product product, int quantity) {
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);
    }
}
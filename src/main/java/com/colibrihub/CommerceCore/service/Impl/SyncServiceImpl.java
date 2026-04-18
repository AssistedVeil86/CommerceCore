package com.colibrihub.CommerceCore.service.Impl;

import com.colibrihub.CommerceCore.Exception.InvalidOperationException;
import com.colibrihub.CommerceCore.Exception.SyncException;
import com.colibrihub.CommerceCore.Exception.UnAuthorizedException;
import com.colibrihub.CommerceCore.dto.Response.SyncResponse;
import com.colibrihub.CommerceCore.dto.Response.WooProductResponse;
import com.colibrihub.CommerceCore.entity.Product;
import com.colibrihub.CommerceCore.mapper.ProductMapper;
import com.colibrihub.CommerceCore.repository.ProductRepository;
import com.colibrihub.CommerceCore.service.SyncService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SyncServiceImpl implements SyncService {

    private final WebClient webClient;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public SyncServiceImpl(
            WebClient webClient,
            ProductRepository productRepository,
            ProductMapper productMapper) {
        this.webClient = webClient;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public SyncResponse syncProducts() {
        int totalSynced = 0;
        int currentPage = 1;
        long totalPages;

        do {
            int finalCurrentPage = currentPage;
            var response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/wp-json/wc/v3/products")
                            .queryParam("page", finalCurrentPage)
                            .queryParam("per_page", 100)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            Mono.error(new UnAuthorizedException("WooCommerce Auth Error: Check Credentials")))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            Mono.error(new SyncException("Error Trying to Connect to WooCommerce")))
                    .toEntityList(WooProductResponse.class)
                    .block();

            if (response == null || !response.hasBody()) {
                throw new InvalidOperationException("Empty response from WooCommerce at page " + finalCurrentPage);
            }

            var wooProducts = response.getBody();
            var totalHeader = response.getHeaders().getFirst("X-WP-TotalPages");

            if (totalHeader == null) {
                throw new InvalidOperationException("Missing X-WP-TotalPages header from WooCommerce");
            }

            totalPages = Long.parseLong(totalHeader);
            int syncedBatchCount = processProducts(wooProducts);

            totalSynced += syncedBatchCount;
            currentPage++;
        } while (currentPage <= totalPages);

        return new SyncResponse(totalSynced, "Sync Completed!");
    }

    @Scheduled(fixedRate = 300000)
    public void scheduledSync() {
        try {
            var result = syncProducts();
            log.info("Scheduled sync completed: {} products synced", result.getTotalSynced());
        } catch (SyncException e) {
            log.error("Scheduled sync failed - WooCommerce error: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Scheduled sync failed - Unexpected error: {}", e.getMessage());
        }
    }

    private List<String> extractProductsSkus(List<WooProductResponse> products){
        return products.stream().map(WooProductResponse::getSku).toList();
    }

    private Map<String, Product> listToProductMap(List<Product> products) {
        return products.stream()
                .collect(Collectors.toMap(Product::getSku, product -> product));
    }

    @CacheEvict(value = { "product", "pagedProducts", "lowStockAlerts" }, allEntries = true)
    private int processProducts(List<WooProductResponse> wooProducts){
        var skus = extractProductsSkus(wooProducts);
        var existingProducts = listToProductMap(productRepository.findAllBySkuIn(skus));

        var productsToSave = wooProducts.stream()
                .map(wooProduct -> {
                    var entity = existingProducts.getOrDefault(wooProduct.getSku(), new Product());
                    return productMapper.updateEntity(entity, wooProduct);
                })
                .toList();

        productRepository.saveAll(productsToSave);

        return productsToSave.size();
    }
}
package com.colibrihub.CommerceCore.repository;

import com.colibrihub.CommerceCore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllBySkuIn(List<String> skus);
    List<Product> findByStockQuantityLessThan(int threshold);
}
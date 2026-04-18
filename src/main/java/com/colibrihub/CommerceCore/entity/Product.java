package com.colibrihub.CommerceCore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Length(max = 1500)
    private String description;
    @Column(nullable = false)
    @Length(max = 1500)
    private String shortDescription;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private BigDecimal regularPrice;
    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate(){
        createdAt = LocalDateTime.now();
    }
}

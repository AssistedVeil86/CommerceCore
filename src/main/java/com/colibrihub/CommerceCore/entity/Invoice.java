package com.colibrihub.CommerceCore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientEmail;
    @Column(nullable = false)
    private String invoiceNumber;
    @Column(nullable = false)
    private BigDecimal subTotal;
    @Column(nullable = false)
    private BigDecimal iva;
    @Column(nullable = false)
    private BigDecimal total;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<InvoiceItem> items;

    @PrePersist
    private void onCreate(){
        createdAt = LocalDateTime.now();
    }
}

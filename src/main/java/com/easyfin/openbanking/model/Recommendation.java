package com.easyfin.openbanking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Recommendation entity for actionable financial advice
 */
@Entity
@Table(name = "recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000, nullable = false)
    private String description;
    
    @Column(nullable = false)
    private String category; // tax-savings, cost-reduction, cash-flow, optimization
    
    @Column(precision = 15, scale = 2)
    private BigDecimal potentialSavings;
    
    @Column(nullable = false)
    private String priority; // high, medium, low
    
    @Column(columnDefinition = "boolean default false")
    private Boolean isActedUpon = false;
    
    private LocalDateTime actedUponAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}


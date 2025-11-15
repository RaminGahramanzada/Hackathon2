package com.easyfin.openbanking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Cash flow forecast entity for financial predictions
 */
@Entity
@Table(name = "cash_flow_forecasts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashFlowForecast {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;
    
    @Column(nullable = false)
    private LocalDate forecastDate;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal predictedIncome;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal predictedExpenses;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal predictedBalance;
    
    private Double confidence;
    
    @Column(length = 500)
    private String notes;
    
    @Column(updatable = false)
    private LocalDate createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}


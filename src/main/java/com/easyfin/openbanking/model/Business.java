package com.easyfin.openbanking.model;

import com.easyfin.openbanking.enums.BusinessType;
import com.easyfin.openbanking.enums.TaxStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Business entity representing a small business owner profile
 */
@Entity
@Table(name = "businesses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Business {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String businessName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessType businessType;
    
    @Column(nullable = false)
    private Integer employeeCount;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal annualIncome;
    
    @Column(length = 3)
    private String currency = "AZN";
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaxStatus taxStatus;
    
    private Double taxExemption;
    
    private String ownerName;
    
    private String email;
    
    private String phone;
    
    private String address;
    
    private String taxId;
    
    private LocalDate registrationDate;
    
    @Column(columnDefinition = "boolean default true")
    private Boolean isActive = true;
    
    @Column(updatable = false)
    private LocalDate createdAt;
    
    private LocalDate updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}


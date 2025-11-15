package com.easyfin.openbanking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Payroll entity for employee salary processing
 */
@Entity
@Table(name = "payroll")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
    
    @Column(nullable = false)
    private LocalDate payrollMonth;
    
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal grossSalary;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal employeeSsfContribution; // 3%
    
    @Column(precision = 10, scale = 2)
    private BigDecimal employerSsfContribution; // 22%
    
    @Column(precision = 10, scale = 2)
    private BigDecimal incomeTax;
    
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal netSalary;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal totalEmployerCost;
    
    @Column(columnDefinition = "boolean default false")
    private Boolean isPaid = false;
    
    private LocalDate paidDate;
    
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


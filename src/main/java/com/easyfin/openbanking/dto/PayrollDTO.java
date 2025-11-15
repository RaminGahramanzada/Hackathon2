package com.easyfin.openbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate payrollMonth;
    private BigDecimal grossSalary;
    private BigDecimal employeeSsfContribution;
    private BigDecimal employerSsfContribution;
    private BigDecimal incomeTax;
    private BigDecimal netSalary;
    private BigDecimal totalEmployerCost;
    private Boolean isPaid;
    private LocalDate paidDate;
}


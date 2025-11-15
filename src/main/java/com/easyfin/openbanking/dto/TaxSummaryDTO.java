package com.easyfin.openbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxSummaryDTO {
    private String taxStatus;
    private Double taxExemptionRate;
    private BigDecimal totalIncome;
    private BigDecimal taxableIncome;
    private BigDecimal totalDeductions;
    private BigDecimal estimatedTaxLiability;
    private BigDecimal estimatedTaxSavings;
    private BigDecimal employeeTaxWithheld;
    private BigDecimal ssfContributions;
    private String period;
}


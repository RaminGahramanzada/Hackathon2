package com.easyfin.openbanking.service;

import com.easyfin.openbanking.dto.TaxSummaryDTO;
import com.easyfin.openbanking.enums.TaxStatus;
import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.model.Transaction;
import com.easyfin.openbanking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for tax calculations (Azerbaijan-specific)
 */
@Service
@RequiredArgsConstructor
public class TaxCalculationService {
    
    private final TransactionRepository transactionRepository;
    
    private static final BigDecimal CORPORATE_TAX_RATE = new BigDecimal("0.20"); // 20%
    
    /**
     * Calculate tax summary for a business
     */
    public TaxSummaryDTO calculateTaxSummary(Business business, LocalDateTime startDate, LocalDateTime endDate) {
        // Get all transactions in period
        BigDecimal totalIncome = transactionRepository.sumIncomeByBusinessIdAndDateRange(
                business.getId(), startDate, endDate);
        BigDecimal totalExpenses = transactionRepository.sumExpensesByBusinessIdAndDateRange(
                business.getId(), startDate, endDate);
        BigDecimal taxDeductible = transactionRepository.sumTaxDeductibleByBusinessIdAndDateRange(
                business.getId(), startDate, endDate);
        
        // Handle nulls
        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpenses = totalExpenses != null ? totalExpenses : BigDecimal.ZERO;
        taxDeductible = taxDeductible != null ? taxDeductible : BigDecimal.ZERO;
        
        // Calculate taxable income
        BigDecimal taxableIncome = totalIncome.subtract(taxDeductible);
        
        // Apply tax exemption for micro-entrepreneurs (75% exemption)
        Double exemptionRate = business.getTaxExemption() != null ? business.getTaxExemption() : 0.0;
        BigDecimal taxableAfterExemption = taxableIncome.multiply(BigDecimal.valueOf(1 - exemptionRate));
        
        // Calculate tax liability
        BigDecimal estimatedTaxLiability = taxableAfterExemption.multiply(CORPORATE_TAX_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        
        // Calculate savings from exemption
        BigDecimal estimatedTaxSavings = taxableIncome.multiply(BigDecimal.valueOf(exemptionRate))
                .multiply(CORPORATE_TAX_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        
        TaxSummaryDTO summary = new TaxSummaryDTO();
        summary.setTaxStatus(business.getTaxStatus().getDisplayName());
        summary.setTaxExemptionRate(exemptionRate);
        summary.setTotalIncome(totalIncome);
        summary.setTaxableIncome(taxableAfterExemption);
        summary.setTotalDeductions(taxDeductible);
        summary.setEstimatedTaxLiability(estimatedTaxLiability);
        summary.setEstimatedTaxSavings(estimatedTaxSavings);
        summary.setEmployeeTaxWithheld(BigDecimal.ZERO); // Placeholder
        summary.setSsfContributions(BigDecimal.ZERO); // Placeholder
        summary.setPeriod(startDate.toLocalDate() + " to " + endDate.toLocalDate());
        
        return summary;
    }
    
    /**
     * Check if business qualifies for micro-entrepreneur status
     */
    public boolean checkMicroEntrepreneurEligibility(Business business, int employeeCount, BigDecimal annualIncome) {
        // Micro-entrepreneur criteria: 1-10 employees, < 200,000 AZN
        return employeeCount >= 1 && employeeCount <= 10 && 
               annualIncome.compareTo(new BigDecimal("200000")) < 0;
    }
    
    /**
     * Calculate potential tax savings
     */
    public BigDecimal calculateTaxSavings(BigDecimal income, TaxStatus taxStatus) {
        BigDecimal taxableIncome = income.multiply(BigDecimal.valueOf(1 - taxStatus.getTaxExemptionRate()));
        BigDecimal savingsFromExemption = income.subtract(taxableIncome).multiply(CORPORATE_TAX_RATE);
        return savingsFromExemption.setScale(2, RoundingMode.HALF_UP);
    }
}


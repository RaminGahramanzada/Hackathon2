package com.easyfin.openbanking.controller;

import com.easyfin.openbanking.dto.TaxSummaryDTO;
import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.model.Transaction;
import com.easyfin.openbanking.repository.BusinessRepository;
import com.easyfin.openbanking.service.TaxCalculationService;
import com.easyfin.openbanking.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Tax management controller (Azerbaijan-specific)
 */
@RestController
@RequestMapping("/api/v1/tax")
@RequiredArgsConstructor
@Tag(name = "Tax Management", description = "Tax calculation and deduction endpoints")
public class TaxController {
    
    private final TaxCalculationService taxCalculationService;
    private final BusinessRepository businessRepository;
    private final TransactionService transactionService;
    
    @GetMapping("/summary")
    @Operation(summary = "Get tax summary", description = "Returns tax summary with sadələşdirilmiş vergi calculations")
    public ResponseEntity<TaxSummaryDTO> getTaxSummary() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(3); // Quarterly
        
        TaxSummaryDTO summary = taxCalculationService.calculateTaxSummary(business, startDate, endDate);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/micro-entrepreneur-status")
    @Operation(summary = "Check 75% exemption eligibility")
    public ResponseEntity<Map<String, Object>> checkMicroEntrepreneurStatus() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        boolean eligible = taxCalculationService.checkMicroEntrepreneurEligibility(
                business, business.getEmployeeCount(), business.getAnnualIncome());
        
        Map<String, Object> status = new HashMap<>();
        status.put("isEligible", eligible);
        status.put("currentStatus", business.getTaxStatus().getDisplayName());
        status.put("taxExemption", business.getTaxExemption());
        status.put("employeeCount", business.getEmployeeCount());
        status.put("annualIncome", business.getAnnualIncome());
        status.put("description", "Micro-entrepreneurs (1-10 employees, <200K AZN) get 75% income tax exemption");
        
        return ResponseEntity.ok(status);
    }
    
    @PostMapping("/categorize/{transactionId}")
    @Operation(summary = "Mark transaction as tax deductible")
    public ResponseEntity<Map<String, Object>> markAsTaxDeductible(
            @PathVariable Long transactionId,
            @RequestParam boolean isTaxDeductible) {
        
        Transaction transaction = transactionService.getTransactionById(transactionId);
        transaction.setIsTaxDeductible(isTaxDeductible);
        Transaction updated = transactionService.saveTransaction(transaction);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", updated.getId());
        response.put("merchantName", updated.getMerchantName());
        response.put("amount", updated.getAmount());
        response.put("category", updated.getCategory());
        response.put("isTaxDeductible", updated.getIsTaxDeductible());
        response.put("message", "Tax deductible status updated successfully");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/deadlines")
    @Operation(summary = "Get upcoming tax deadlines")
    public ResponseEntity<Map<String, Object>> getTaxDeadlines() {
        Map<String, Object> deadlines = new HashMap<>();
        deadlines.put("nextDeadline", "2024-12-20");
        deadlines.put("type", "Quarterly Declaration");
        deadlines.put("daysRemaining", 15);
        deadlines.put("description", "Quarterly tax declaration to DVX (Dövlət Vergi Xidməti)");
        
        return ResponseEntity.ok(deadlines);
    }
    
    @PostMapping("/export-report")
    @Operation(summary = "Generate tax report PDF")
    public ResponseEntity<String> exportTaxReport() {
        return ResponseEntity.ok("Tax report generated: /reports/tax-report-2024-Q4.pdf");
    }
    
    @GetMapping("/dvx-integration")
    @Operation(summary = "Get DVX mock data")
    public ResponseEntity<Map<String, Object>> getDvxIntegration() {
        Map<String, Object> dvxData = new HashMap<>();
        dvxData.put("status", "connected");
        dvxData.put("lastSync", LocalDateTime.now().minusHours(2));
        dvxData.put("declarationsSubmitted", 4);
        dvxData.put("nextDeadline", "2024-12-20");
        
        return ResponseEntity.ok(dvxData);
    }
    
    @GetMapping("/deductions")
    @Operation(summary = "Get potential tax deductions")
    public ResponseEntity<Map<String, Object>> getTaxDeductions() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        TaxSummaryDTO summary = taxCalculationService.calculateTaxSummary(
                business, LocalDateTime.now().minusMonths(3), LocalDateTime.now());
        
        Map<String, Object> deductions = new HashMap<>();
        deductions.put("totalDeductions", summary.getTotalDeductions());
        deductions.put("estimatedSavings", summary.getEstimatedTaxSavings());
        deductions.put("categories", Map.of(
                "Food Supplies", "12,500 AZN",
                "Rent", "9,000 AZN",
                "Utilities", "2,400 AZN",
                "Equipment", "1,650 AZN"
        ));
        
        return ResponseEntity.ok(deductions);
    }
    
    @GetMapping("/ready")
    @Operation(summary = "Get Tax Ready page data (complete data for 2024 expenses)")
    public ResponseEntity<Map<String, Object>> getTaxReadyData() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonth = now.minusMonths(1);
        LocalDateTime lastMonthStart = lastMonth.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime lastMonthEnd = lastMonth.withDayOfMonth(lastMonth.toLocalDate().lengthOfMonth())
                .withHour(23).withMinute(59).withSecond(59);
        
        // Get all deductible transactions for 2024
        java.util.List<Transaction> allTransactions = transactionService.getDeductibleTransactions(business.getId());
        
        // Calculate total deductible
        java.math.BigDecimal totalDeductible = allTransactions.stream()
                .filter(t -> t.getIsTaxDeductible())
                .map(Transaction::getAmount)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        // Calculate last month deductible
        java.math.BigDecimal lastMonthDeductible = allTransactions.stream()
                .filter(t -> t.getIsTaxDeductible())
                .filter(t -> t.getTransactionDate().isAfter(lastMonthStart) && 
                            t.getTransactionDate().isBefore(lastMonthEnd))
                .map(Transaction::getAmount)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        // Calculate category breakdowns
        java.util.Map<String, java.math.BigDecimal> categoryBreakdown = new java.util.HashMap<>();
        for (Transaction t : allTransactions) {
            if (t.getIsTaxDeductible() && t.getCategory() != null) {
                String categoryName = getCategoryDisplayName(t.getCategory().name());
                categoryBreakdown.merge(categoryName, t.getAmount(), java.math.BigDecimal::add);
            }
        }
        
        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("year", 2024);
        response.put("totalDeductible", totalDeductible);
        response.put("lastMonthChange", lastMonthDeductible);
        response.put("categoryBreakdown", categoryBreakdown);
        
        // Add recommendation
        Map<String, Object> recommendation = new HashMap<>();
        recommendation.put("type", "SIMPLIFIED_TAXATION");
        recommendation.put("title", "Simplified Taxation recommended");
        recommendation.put("message", "Based on your expense profile, simplified taxation (sadələşdirilmiş vergi) could save you more");
        recommendation.put("actionUrl", "/recommendations");
        response.put("recommendation", recommendation);
        
        // Add recent deductible transactions (limit 10)
        java.util.List<Map<String, Object>> recentTransactions = new java.util.ArrayList<>();
        allTransactions.stream()
                .filter(t -> t.getIsTaxDeductible())
                .sorted((a, b) -> b.getTransactionDate().compareTo(a.getTransactionDate()))
                .limit(10)
                .forEach(t -> {
                    Map<String, Object> txn = new HashMap<>();
                    txn.put("id", t.getId());
                    txn.put("merchantName", t.getMerchantName());
                    txn.put("description", t.getDescription());
                    txn.put("amount", t.getAmount());
                    txn.put("date", t.getTransactionDate().toLocalDate());
                    txn.put("category", t.getCategory() != null ? t.getCategory().name() : "OTHER");
                    txn.put("isDeductible", t.getIsTaxDeductible());
                    recentTransactions.add(txn);
                });
        response.put("recentTransactions", recentTransactions);
        
        return ResponseEntity.ok(response);
    }
    
    private String getCategoryDisplayName(String category) {
        switch (category) {
            case "FOOD_SUPPLIES": return "Business Expenses";
            case "OFFICE_SUPPLIES": return "Office Supplies";
            case "RENT": return "Rent";
            case "UTILITIES": return "Utilities";
            case "TELECOMMUNICATIONS": return "Telecommunications";
            case "EQUIPMENT": return "Equipment";
            case "SALARIES": return "Salaries";
            case "MARKETING": return "Marketing";
            default: return "Other";
        }
    }
}


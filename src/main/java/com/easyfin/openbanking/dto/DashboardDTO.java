package com.easyfin.openbanking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dashboard summary with user profile, balance, and financial metrics")
public class DashboardDTO {
    
    // User Information
    @Schema(description = "Business owner's first name", example = "Rail")
    private String firstName;
    
    @Schema(description = "Business owner's last name", example = "Nuriyev")
    private String lastName;
    
    @Schema(description = "Business owner's full name", example = "Rail Nuriyev")
    private String fullName;
    
    // Balance Information
    @Schema(description = "Total balance in account (income - expenses)", example = "45300.00")
    private BigDecimal totalBalance;
    
    @Schema(description = "Current usable money (Hazirda hesabda olan pul)", example = "29400.00")
    private BigDecimal availableBalance;
    
    @Schema(description = "Money in tax processing, available in ~2 weeks (Vergide qalan pul)", example = "8900.00")
    private BigDecimal pendingBalance;
    
    // Financial Metrics
    @Schema(description = "Total income for the period", example = "48000.00")
    private BigDecimal totalIncome;
    
    @Schema(description = "Total expenses for the period", example = "32450.00")
    private BigDecimal totalExpenses;
    
    @Schema(description = "Net cash flow (income - expenses)", example = "15550.00")
    private BigDecimal netCashFlow;
    
    @Schema(description = "Total tax-deductible expenses", example = "28400.00")
    private BigDecimal taxDeductibleExpenses;
    
    @Schema(description = "Estimated tax savings from deductions", example = "27750.00")
    private BigDecimal estimatedTaxSavings;
    
    // Summary Info
    @Schema(description = "Number of active employees", example = "7")
    private Integer activeEmployees;
    
    @Schema(description = "Number of unread alerts", example = "3")
    private Integer unreadAlerts;
    
    @Schema(description = "Number of pending recommendations", example = "3")
    private Integer pendingRecommendations;
    
    // Recent Activity
    @Schema(description = "List of recent transactions")
    private List<RecentTransaction> recentTransactions;
    
    @Schema(description = "Spending breakdown by category")
    private Map<String, BigDecimal> spendingByCategory;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Recent transaction summary")
    public static class RecentTransaction {
        @Schema(description = "Transaction ID", example = "1")
        private Long id;
        
        @Schema(description = "Merchant name", example = "Taze Bazar")
        private String merchantName;
        
        @Schema(description = "Transaction amount", example = "250.00")
        private BigDecimal amount;
        
        @Schema(description = "Transaction category", example = "Food Supplies")
        private String category;
        
        @Schema(description = "Transaction date", example = "2024-11-14")
        private String date;
    }
}


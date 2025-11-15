package com.easyfin.openbanking.service;

import com.easyfin.openbanking.dto.DashboardDTO;
import com.easyfin.openbanking.enums.TransactionCategory;
import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.model.Transaction;
import com.easyfin.openbanking.repository.AlertRepository;
import com.easyfin.openbanking.repository.EmployeeRepository;
import com.easyfin.openbanking.repository.RecommendationRepository;
import com.easyfin.openbanking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for dashboard data aggregation
 */
@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final TransactionRepository transactionRepository;
    private final EmployeeRepository employeeRepository;
    private final AlertRepository alertRepository;
    private final RecommendationRepository recommendationRepository;
    private final TaxCalculationService taxCalculationService;
    
    /**
     * Get dashboard summary
     */
    public DashboardDTO getDashboardSummary(Business business) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(30);
        
        // Get financial metrics for the period
        BigDecimal totalIncome = transactionRepository.sumIncomeByBusinessIdAndDateRange(
                business.getId(), startDate, endDate);
        BigDecimal totalExpenses = transactionRepository.sumExpensesByBusinessIdAndDateRange(
                business.getId(), startDate, endDate);
        BigDecimal taxDeductible = transactionRepository.sumTaxDeductibleByBusinessIdAndDateRange(
                business.getId(), startDate, endDate);
        
        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpenses = totalExpenses != null ? totalExpenses : BigDecimal.ZERO;
        taxDeductible = taxDeductible != null ? taxDeductible : BigDecimal.ZERO;
        
        BigDecimal netCashFlow = totalIncome.subtract(totalExpenses);
        BigDecimal estimatedTaxSavings = taxCalculationService.calculateTaxSavings(
                totalIncome, business.getTaxStatus());
        
        // Calculate balance information
        // Total Balance = All income - all expenses
        BigDecimal totalBalance = totalIncome.subtract(totalExpenses);
        
        // Pending Balance = Tax deductible amount (money in tax processing)
        // This money will be available after tax filing (~2 weeks)
        BigDecimal pendingBalance = taxDeductible.multiply(new BigDecimal("0.20")); // 20% tax benefit
        
        // Available Balance = Total - Pending (current usable money)
        BigDecimal availableBalance = totalBalance.subtract(pendingBalance);
        
        // Get counts
        Integer activeEmployees = employeeRepository.countByBusinessIdAndIsActiveTrue(business.getId()).intValue();
        Integer unreadAlerts = alertRepository.countByBusinessIdAndIsDismissedFalse(business.getId()).intValue();
        Integer pendingRecommendations = recommendationRepository.findByBusinessIdAndIsActedUponFalse(business.getId()).size();
        
        // Get recent transactions
        List<Transaction> recentTrans = transactionRepository.findByBusinessIdOrderByTransactionDateDesc(business.getId())
                .stream()
                .limit(5)
                .collect(Collectors.toList());
        
        List<DashboardDTO.RecentTransaction> recentTransactions = recentTrans.stream()
                .map(t -> new DashboardDTO.RecentTransaction(
                        t.getId(),
                        t.getMerchantName(),
                        t.getAmount(),
                        t.getCategory().getDisplayName(),
                        t.getTransactionDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
                ))
                .collect(Collectors.toList());
        
        // Get spending by category
        List<Object[]> categoryData = transactionRepository.sumExpensesByCategory(
                business.getId(), startDate, endDate);
        Map<String, BigDecimal> spendingByCategory = new HashMap<>();
        for (Object[] row : categoryData) {
            TransactionCategory category = (TransactionCategory) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            spendingByCategory.put(category.getDisplayName(), amount);
        }
        
        // Build DTO
        DashboardDTO dto = new DashboardDTO();
        
        // User Information
        String ownerName = business.getOwnerName() != null ? business.getOwnerName() : "Business Owner";
        String[] nameParts = ownerName.split(" ", 2);
        dto.setFirstName(nameParts.length > 0 ? nameParts[0] : "Business");
        dto.setLastName(nameParts.length > 1 ? nameParts[1] : "Owner");
        dto.setFullName(ownerName);
        
        // Balance Information (iOS app format)
        dto.setTotalBalance(totalBalance);
        dto.setAvailableBalance(availableBalance);
        dto.setPendingBalance(pendingBalance);
        
        // Financial Metrics
        dto.setTotalIncome(totalIncome);
        dto.setTotalExpenses(totalExpenses);
        dto.setNetCashFlow(netCashFlow);
        dto.setTaxDeductibleExpenses(taxDeductible);
        dto.setEstimatedTaxSavings(estimatedTaxSavings);
        
        // Summary Info
        dto.setActiveEmployees(activeEmployees);
        dto.setUnreadAlerts(unreadAlerts);
        dto.setPendingRecommendations(pendingRecommendations);
        
        // Recent Activity
        dto.setRecentTransactions(recentTransactions);
        dto.setSpendingByCategory(spendingByCategory);
        
        return dto;
    }
}


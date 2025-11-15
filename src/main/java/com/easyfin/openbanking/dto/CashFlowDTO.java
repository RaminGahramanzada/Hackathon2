package com.easyfin.openbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashFlowDTO {
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netCashFlow;
    private BigDecimal currentBalance;
    
    // Category breakdowns - shows where money comes from/goes to
    private Map<String, BigDecimal> incomeByCategory;
    private Map<String, BigDecimal> expensesByCategory;
    
    private List<ForecastItem> forecast;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastItem {
        private LocalDate date;
        private BigDecimal predictedIncome;
        private BigDecimal predictedExpenses;
        private BigDecimal predictedBalance;
        private Double confidence;
    }
}


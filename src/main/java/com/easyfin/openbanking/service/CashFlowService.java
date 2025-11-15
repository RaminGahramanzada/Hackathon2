package com.easyfin.openbanking.service;

import com.easyfin.openbanking.dto.CashFlowDTO;
import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.model.CashFlowForecast;
import com.easyfin.openbanking.repository.CashFlowForecastRepository;
import com.easyfin.openbanking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for cash flow forecasting and analysis
 */
@Service
@RequiredArgsConstructor
public class CashFlowService {
    
    private final TransactionRepository transactionRepository;
    private final CashFlowForecastRepository forecastRepository;
    
    /**
     * Generate cash flow analysis with category breakdowns
     */
    public CashFlowDTO generateCashFlowAnalysis(Business business, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal totalIncome = transactionRepository.sumIncomeByBusinessIdAndDateRange(
                business.getId(), startDate, endDate);
        BigDecimal totalExpenses = transactionRepository.sumExpensesByBusinessIdAndDateRange(
                business.getId(), startDate, endDate);
        
        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpenses = totalExpenses != null ? totalExpenses : BigDecimal.ZERO;
        
        BigDecimal netCashFlow = totalIncome.subtract(totalExpenses);
        
        CashFlowDTO dto = new CashFlowDTO();
        dto.setPeriodStart(startDate.toLocalDate());
        dto.setPeriodEnd(endDate.toLocalDate());
        dto.setTotalIncome(totalIncome);
        dto.setTotalExpenses(totalExpenses);
        dto.setNetCashFlow(netCashFlow);
        dto.setCurrentBalance(netCashFlow); // Simplified
        
        // Calculate income breakdown by category
        var incomeBreakdown = transactionRepository.findByBusinessIdAndIsIncomeTrueAndTransactionDateBetween(
                business.getId(), startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory() != null ? t.getCategory().name() : "Other",
                        Collectors.reducing(BigDecimal.ZERO,
                                t -> t.getAmount(),
                                BigDecimal::add)
                ));
        dto.setIncomeByCategory(incomeBreakdown);
        
        // Calculate expense breakdown by category
        var expenseBreakdown = transactionRepository.findByBusinessIdAndIsIncomeFalseAndTransactionDateBetween(
                business.getId(), startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory() != null ? t.getCategory().name() : "Other",
                        Collectors.reducing(BigDecimal.ZERO,
                                t -> t.getAmount(),
                                BigDecimal::add)
                ));
        dto.setExpensesByCategory(expenseBreakdown);
        
        // Get forecasts
        List<CashFlowForecast> forecasts = forecastRepository.findFutureForecasts(
                business.getId(), LocalDate.now());
        dto.setForecast(forecasts.stream()
                .map(f -> new CashFlowDTO.ForecastItem(
                        f.getForecastDate(),
                        f.getPredictedIncome(),
                        f.getPredictedExpenses(),
                        f.getPredictedBalance(),
                        f.getConfidence()
                ))
                .collect(Collectors.toList()));
        
        return dto;
    }
    
    /**
     * Generate cash flow forecast for next N days
     */
    public List<CashFlowForecast> generateForecast(Business business, int days) {
        List<CashFlowForecast> forecasts = new ArrayList<>();
        
        // Get historical averages
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(30);
        
        BigDecimal avgIncome = transactionRepository.sumIncomeByBusinessIdAndDateRange(
                business.getId(), startDate, endDate);
        BigDecimal avgExpenses = transactionRepository.sumExpensesByBusinessIdAndDateRange(
                business.getId(), startDate, endDate);
        
        avgIncome = avgIncome != null ? avgIncome.divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        avgExpenses = avgExpenses != null ? avgExpenses.divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        
        // Generate forecasts for next N days
        BigDecimal runningBalance = avgIncome.subtract(avgExpenses).multiply(BigDecimal.valueOf(30)); // Starting balance
        
        for (int i = 1; i <= days; i++) {
            LocalDate forecastDate = LocalDate.now().plusDays(i);
            
            CashFlowForecast forecast = new CashFlowForecast();
            forecast.setBusiness(business);
            forecast.setForecastDate(forecastDate);
            forecast.setPredictedIncome(avgIncome);
            forecast.setPredictedExpenses(avgExpenses);
            
            runningBalance = runningBalance.add(avgIncome).subtract(avgExpenses);
            forecast.setPredictedBalance(runningBalance);
            forecast.setConfidence(0.75); // 75% confidence
            
            forecasts.add(forecast);
        }
        
        // Save all forecasts
        return forecastRepository.saveAll(forecasts);
    }
}


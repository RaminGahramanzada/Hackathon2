package com.easyfin.openbanking.controller;

import com.easyfin.openbanking.dto.CashFlowDTO;
import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.model.CashFlowForecast;
import com.easyfin.openbanking.repository.BusinessRepository;
import com.easyfin.openbanking.service.CashFlowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Cash flow forecasting controller
 */
@RestController
@RequestMapping("/api/v1/cashflow")
@RequiredArgsConstructor
@Tag(name = "Cash Flow", description = "Cash flow forecasting endpoints")
public class CashFlowController {
    
    private final CashFlowService cashFlowService;
    private final BusinessRepository businessRepository;
    
    @GetMapping("/forecast")
    @Operation(summary = "Get 30-60 day cash flow forecast")
    public ResponseEntity<List<CashFlowForecast>> getForecast(@RequestParam(defaultValue = "60") int days) {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<CashFlowForecast> forecasts = cashFlowService.generateForecast(business, days);
        return ResponseEntity.ok(forecasts);
    }
    
    @GetMapping("/analysis")
    @Operation(summary = "Get cash flow analysis for period")
    public ResponseEntity<CashFlowDTO> getCashFlowAnalysis() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(30);
        
        CashFlowDTO analysis = cashFlowService.generateCashFlowAnalysis(business, startDate, endDate);
        return ResponseEntity.ok(analysis);
    }
    
    @GetMapping("/predictions")
    @Operation(summary = "Predict shortfalls/surpluses")
    public ResponseEntity<List<CashFlowForecast>> getPredictions() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<CashFlowForecast> predictions = cashFlowService.generateForecast(business, 30);
        return ResponseEntity.ok(predictions);
    }
    
    @GetMapping("/trends")
    @Operation(summary = "Get historical cash flow trends")
    public ResponseEntity<CashFlowDTO> getTrends() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(90); // 3 months
        
        CashFlowDTO trends = cashFlowService.generateCashFlowAnalysis(business, startDate, endDate);
        return ResponseEntity.ok(trends);
    }
    
    @GetMapping("/forecast-summary")
    @Operation(summary = "Get cash flow forecast page summary (all data for forecast page)")
    public ResponseEntity<java.util.Map<String, Object>> getForecastSummary() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        // Generate 30-day forecast
        List<CashFlowForecast> forecasts = cashFlowService.generateForecast(business, 30);
        
        // Calculate average daily income and expenses
        java.math.BigDecimal avgDailyIncome = java.math.BigDecimal.ZERO;
        java.math.BigDecimal avgDailyExpenses = java.math.BigDecimal.ZERO;
        
        if (!forecasts.isEmpty()) {
            for (CashFlowForecast forecast : forecasts) {
                avgDailyIncome = avgDailyIncome.add(forecast.getPredictedIncome());
                avgDailyExpenses = avgDailyExpenses.add(forecast.getPredictedExpenses());
            }
            avgDailyIncome = avgDailyIncome.divide(java.math.BigDecimal.valueOf(forecasts.size()), 2, java.math.RoundingMode.HALF_UP);
            avgDailyExpenses = avgDailyExpenses.divide(java.math.BigDecimal.valueOf(forecasts.size()), 2, java.math.RoundingMode.HALF_UP);
        }
        
        // Prepare response
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("avgDailyIncome", avgDailyIncome);
        response.put("avgDailyExpenses", avgDailyExpenses);
        response.put("period", "Next 30 days");
        response.put("forecast", forecasts);
        
        // Add upcoming alerts (simplified)
        java.util.List<java.util.Map<String, Object>> alerts = new java.util.ArrayList<>();
        
        // Check for potential cash shortage
        boolean hasShortage = false;
        for (CashFlowForecast forecast : forecasts) {
            if (forecast.getPredictedBalance().compareTo(java.math.BigDecimal.valueOf(2000)) < 0) {
                hasShortage = true;
                break;
            }
        }
        
        if (hasShortage) {
            java.util.Map<String, Object> shortageAlert = new java.util.HashMap<>();
            shortageAlert.put("type", "CASH_SHORTAGE");
            shortageAlert.put("title", "Cash Shortage Alert");
            shortageAlert.put("message", "Balance may drop below $2,000");
            shortageAlert.put("severity", "high");
            alerts.add(shortageAlert);
        }
        
        // Tax payment due alert
        java.util.Map<String, Object> taxAlert = new java.util.HashMap<>();
        taxAlert.put("type", "TAX_PAYMENT");
        taxAlert.put("title", "Tax Payment Due");
        taxAlert.put("message", "Quarterly sales tax: $1,850");
        taxAlert.put("severity", "medium");
        taxAlert.put("dueDate", java.time.LocalDate.now().plusDays(15).toString());
        alerts.add(taxAlert);
        
        // Large payment alert
        java.util.Map<String, Object> paymentAlert = new java.util.HashMap<>();
        paymentAlert.put("type", "LARGE_PAYMENT");
        paymentAlert.put("title", "Large Payment");
        paymentAlert.put("message", "Equipment lease: $3,200");
        paymentAlert.put("severity", "medium");
        paymentAlert.put("dueDate", java.time.LocalDate.now().plusDays(20).toString());
        alerts.add(paymentAlert);
        
        response.put("upcomingAlerts", alerts);
        
        return ResponseEntity.ok(response);
    }
}


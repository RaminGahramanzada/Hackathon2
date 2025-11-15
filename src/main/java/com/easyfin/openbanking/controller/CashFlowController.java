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
}


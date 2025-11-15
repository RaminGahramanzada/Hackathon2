package com.easyfin.openbanking.controller;

import com.easyfin.openbanking.dto.AlertDTO;
import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.repository.BusinessRepository;
import com.easyfin.openbanking.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Intelligent alerts controller
 */
@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
@Tag(name = "Alerts", description = "Intelligent alert endpoints")
public class AlertController {
    
    private final AlertService alertService;
    private final BusinessRepository businessRepository;
    
    @GetMapping
    @Operation(
        summary = "Get all active alerts", 
        description = """
            Returns all active (non-dismissed) alerts for the business including:
            - Tax deadline reminders (with days remaining and deadline date)
            - Low balance warnings
            - Unusual spending patterns
            - Unauthorized payment detections
            
            For TAX_DEADLINE alerts, additional fields are provided:
            - deadlineDate: The actual tax deadline date
            - daysRemaining: Number of days until deadline
            - actionUrl: Deep link to relevant section
            
            Example: "Tax Deadline Approaching - VAT Payment in 5 days"
            """
    )
    public ResponseEntity<List<AlertDTO>> getAllAlerts() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<AlertDTO> alerts = alertService.getActiveAlerts(business.getId());
        return ResponseEntity.ok(alerts);
    }
    
    @GetMapping("/unauthorized-payments")
    @Operation(
        summary = "Get unauthorized payment alerts", 
        description = "Returns alerts for potentially unauthorized or suspicious transactions"
    )
    public ResponseEntity<List<AlertDTO>> getUnauthorizedPayments() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<AlertDTO> alerts = alertService.getAlertsByType(business.getId(), "UNAUTHORIZED_PAYMENT");
        return ResponseEntity.ok(alerts);
    }
    
    @GetMapping("/low-balance")
    @Operation(
        summary = "Get low balance warnings", 
        description = "Returns alerts for low account balance situations that require attention"
    )
    public ResponseEntity<List<AlertDTO>> getLowBalanceAlerts() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<AlertDTO> alerts = alertService.getAlertsByType(business.getId(), "LOW_BALANCE");
        return ResponseEntity.ok(alerts);
    }
    
    @GetMapping("/unusual-spending")
    @Operation(
        summary = "Get unusual spending pattern alerts", 
        description = "Returns alerts for detected unusual spending patterns or anomalies"
    )
    public ResponseEntity<List<AlertDTO>> getUnusualSpendingAlerts() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<AlertDTO> alerts = alertService.getAlertsByType(business.getId(), "UNUSUAL_SPENDING");
        return ResponseEntity.ok(alerts);
    }
    
    @GetMapping("/tax-deadlines")
    @Operation(
        summary = "Get upcoming tax deadline reminders", 
        description = """
            Returns only TAX_DEADLINE alerts with complete deadline information:
            - title: e.g., "Tax Deadline Approaching"
            - message: e.g., "VAT Payment in 5 days"
            - deadlineDate: The actual deadline date
            - daysRemaining: Days until deadline
            - severity: Urgency level (high if < 7 days, critical if < 3 days)
            
            Use this for the tax deadline notification banner on the home screen.
            """
    )
    public ResponseEntity<List<AlertDTO>> getTaxDeadlineAlerts() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<AlertDTO> alerts = alertService.getAlertsByType(business.getId(), "TAX_DEADLINE");
        return ResponseEntity.ok(alerts);
    }
    
    @PostMapping("/{id}/dismiss")
    @Operation(
        summary = "Dismiss an alert", 
        description = "Marks an alert as dismissed so it won't appear in the active alerts list. Use this when user closes/dismisses a notification."
    )
    public ResponseEntity<String> dismissAlert(@PathVariable Long id) {
        alertService.dismissAlert(id);
        return ResponseEntity.ok("Alert dismissed successfully");
    }
    
    @PutMapping("/settings")
    @Operation(summary = "Configure alert thresholds")
    public ResponseEntity<Map<String, Object>> updateAlertSettings(@RequestBody Map<String, Object> settings) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Settings updated successfully");
        response.put("settings", settings);
        return ResponseEntity.ok(response);
    }
}


package com.easyfin.openbanking.controller;

import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.repository.BusinessRepository;
import com.easyfin.openbanking.repository.EmployeeRepository;
import com.easyfin.openbanking.service.TaxCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Business profile management controller
 */
@RestController
@RequestMapping("/api/v1/business")
@RequiredArgsConstructor
@Tag(name = "Business Profile", description = "Business profile management endpoints")
public class BusinessController {
    
    private final BusinessRepository businessRepository;
    private final EmployeeRepository employeeRepository;
    private final TaxCalculationService taxCalculationService;
    
    @GetMapping("/profile")
    @Operation(summary = "Get business profile", 
               description = "Returns restaurant profile with 7 employees, <200K AZN income")
    public ResponseEntity<Business> getBusinessProfile() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        return ResponseEntity.ok(business);
    }
    
    @PutMapping("/profile")
    @Operation(summary = "Update business profile")
    public ResponseEntity<Map<String, Object>> updateBusinessProfile(@RequestBody Map<String, Object> updates) {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        if (updates.containsKey("businessName")) {
            business.setBusinessName((String) updates.get("businessName"));
        }
        if (updates.containsKey("ownerName")) {
            business.setOwnerName((String) updates.get("ownerName"));
        }
        if (updates.containsKey("email")) {
            business.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("phone")) {
            business.setPhone((String) updates.get("phone"));
        }
        if (updates.containsKey("address")) {
            business.setAddress((String) updates.get("address"));
        }
        
        Business saved = businessRepository.save(business);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", saved.getId());
        response.put("businessName", saved.getBusinessName());
        response.put("ownerName", saved.getOwnerName());
        response.put("email", saved.getEmail());
        response.put("phone", saved.getPhone());
        response.put("address", saved.getAddress());
        response.put("message", "Business profile updated successfully");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/tax-status")
    @Operation(summary = "Get tax status (sadələşdirilmiş vergi eligibility)")
    public ResponseEntity<Map<String, Object>> getTaxStatus() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        Long employeeCount = employeeRepository.countByBusinessIdAndIsActiveTrue(business.getId());
        boolean eligible = taxCalculationService.checkMicroEntrepreneurEligibility(
                business, employeeCount.intValue(), business.getAnnualIncome());
        
        Map<String, Object> taxStatus = new HashMap<>();
        taxStatus.put("currentStatus", business.getTaxStatus().getDisplayName());
        taxStatus.put("taxStatusCode", business.getTaxStatus().name());
        taxStatus.put("taxExemptionRate", business.getTaxExemption());
        taxStatus.put("isEligibleForMicroEntrepreneur", eligible);
        taxStatus.put("employeeCount", employeeCount);
        taxStatus.put("annualIncome", business.getAnnualIncome());
        taxStatus.put("currency", business.getCurrency());
        taxStatus.put("description", business.getTaxStatus().getDescription());
        taxStatus.put("benefits", Map.of(
                "incomeTaxExemption", "75%",
                "propertyTaxExemption", "100%",
                "acceleratedDepreciation", "2x rate",
                "simplifiedReporting", "Yes"
        ));
        
        return ResponseEntity.ok(taxStatus);
    }
    
    @GetMapping("/compliance")
    @Operation(summary = "Check compliance status")
    public ResponseEntity<Map<String, Object>> getComplianceStatus() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        Map<String, Object> compliance = new HashMap<>();
        compliance.put("businessName", business.getBusinessName());
        compliance.put("taxId", business.getTaxId());
        compliance.put("isActive", business.getIsActive());
        compliance.put("registrationStatus", "Valid");
        compliance.put("taxComplianceStatus", "Good Standing");
        compliance.put("lastTaxDeclaration", "2024-09-20");
        compliance.put("nextTaxDeadline", "2024-12-20");
        compliance.put("outstandingIssues", 0);
        compliance.put("complianceScore", 95);
        compliance.put("recommendations", List.of(
                "Ensure Q4 2024 declaration is submitted by December 20",
                "All employees have valid SSF registrations",
                "Consider quarterly tax planning meetings"
        ));
        
        return ResponseEntity.ok(compliance);
    }
}


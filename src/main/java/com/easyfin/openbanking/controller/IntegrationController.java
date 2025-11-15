package com.easyfin.openbanking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * External integrations controller (Mock implementations)
 */
@RestController
@RequestMapping("/api/v1/integrations")
@RequiredArgsConstructor
@Tag(name = "Integrations", description = "External integration endpoints (DVX, eGov, ASAN İmza)")
public class IntegrationController {
    
    @PostMapping("/dvx/sync")
    @Operation(summary = "Sync with DVX (Dövlət Vergi Xidməti)", 
               description = "Mock integration with Azerbaijan Tax Service")
    public ResponseEntity<Map<String, Object>> syncWithDvx() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("lastSync", LocalDateTime.now());
        response.put("declarationsFound", 4);
        response.put("pendingActions", 1);
        response.put("message", "Successfully synced with DVX");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/dvx/declarations")
    @Operation(summary = "Get tax declarations from DVX")
    public ResponseEntity<List<Map<String, Object>>> getDvxDeclarations() {
        List<Map<String, Object>> declarations = List.of(
                Map.of(
                        "declarationId", "DVX-2024-Q3-001",
                        "quarter", "Q3 2024",
                        "submissionDate", "2024-09-20",
                        "status", "Submitted",
                        "taxAmount", "3,250.00 AZN"
                ),
                Map.of(
                        "declarationId", "DVX-2024-Q2-001",
                        "quarter", "Q2 2024",
                        "submissionDate", "2024-06-20",
                        "status", "Approved",
                        "taxAmount", "2,980.00 AZN"
                )
        );
        
        return ResponseEntity.ok(declarations);
    }
    
    @PostMapping("/egov/business-info")
    @Operation(summary = "Get business info from eGov", 
               description = "Mock integration with Azerbaijan eGov portal")
    public ResponseEntity<Map<String, Object>> getBusinessInfoFromEgov() {
        Map<String, Object> businessInfo = new HashMap<>();
        businessInfo.put("businessName", "Nizami Restaurant");
        businessInfo.put("taxId", "1234567890");
        businessInfo.put("registrationDate", "2022-03-15");
        businessInfo.put("legalForm", "Limited Liability Company");
        businessInfo.put("address", "28 May küç., Baku, Azerbaijan");
        businessInfo.put("status", "Active");
        businessInfo.put("egovVerified", true);
        
        return ResponseEntity.ok(businessInfo);
    }
    
    @GetMapping("/egov/certificates")
    @Operation(summary = "Get business certificates from eGov")
    public ResponseEntity<List<Map<String, Object>>> getEgovCertificates() {
        List<Map<String, Object>> certificates = List.of(
                Map.of(
                        "certificateName", "Business Registration Certificate",
                        "issueDate", "2022-03-15",
                        "expiryDate", "N/A",
                        "status", "Valid",
                        "downloadUrl", "/certificates/registration.pdf"
                ),
                Map.of(
                        "certificateName", "Food Safety Certificate",
                        "issueDate", "2024-01-10",
                        "expiryDate", "2025-01-10",
                        "status", "Valid",
                        "downloadUrl", "/certificates/food-safety.pdf"
                )
        );
        
        return ResponseEntity.ok(certificates);
    }
    
    @PostMapping("/asan-imza/sign")
    @Operation(summary = "Sign document with ASAN İmza/SİMA", 
               description = "Mock digital signature integration")
    public ResponseEntity<Map<String, Object>> signWithAsanImza(@RequestBody Map<String, Object> document) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("signedAt", LocalDateTime.now());
        response.put("documentId", document.get("documentId"));
        response.put("signatureValid", true);
        response.put("signerName", "Murad Aliyev");
        response.put("message", "Document signed successfully with ASAN İmza");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/bank/sync")
    @Operation(summary = "Sync bank transactions (Open Banking)", 
               description = "Mock Open Banking integration")
    public ResponseEntity<Map<String, Object>> syncBankTransactions() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("transactionsSynced", 15);
        response.put("lastSync", LocalDateTime.now());
        response.put("accountsConnected", 1);
        response.put("message", "Bank transactions synced successfully");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/bank/accounts")
    @Operation(summary = "Get connected bank accounts")
    public ResponseEntity<List<Map<String, Object>>> getConnectedBankAccounts() {
        List<Map<String, Object>> accounts = List.of(
                Map.of(
                        "bankName", "Kapital Bank",
                        "accountNumber", "****5678",
                        "accountType", "Business Current Account",
                        "balance", "45,230.50 AZN",
                        "currency", "AZN",
                        "status", "Connected",
                        "lastSync", LocalDateTime.now().minusHours(2)
                )
        );
        
        return ResponseEntity.ok(accounts);
    }
}


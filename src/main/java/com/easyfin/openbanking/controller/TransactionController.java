package com.easyfin.openbanking.controller;

import com.easyfin.openbanking.dto.TransactionDTO;
import com.easyfin.openbanking.enums.TransactionCategory;
import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.model.Transaction;
import com.easyfin.openbanking.repository.BusinessRepository;
import com.easyfin.openbanking.service.CategorizationService;
import com.easyfin.openbanking.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Transaction management controller
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction management endpoints")
public class TransactionController {
    
    private final TransactionService transactionService;
    private final BusinessRepository businessRepository;
    private final CategorizationService categorizationService;
    
    @GetMapping
    @Operation(summary = "Get all transactions", 
               description = "Returns all transactions with optional date and category filters")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) TransactionCategory category) {
        
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<TransactionDTO> transactions;
        if (startDate != null && endDate != null) {
            transactions = transactionService.getTransactionsByDateRange(business.getId(), startDate, endDate);
        } else {
            transactions = transactionService.getAllTransactions(business.getId());
        }
        
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        TransactionDTO dto = convertToDTO(transaction);
        return ResponseEntity.ok(dto);
    }
    
    private TransactionDTO convertToDTO(Transaction t) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(t.getId());
        dto.setTransactionDate(t.getTransactionDate());
        dto.setAmount(t.getAmount());
        dto.setCurrency(t.getCurrency());
        dto.setMerchantName(t.getMerchantName());
        dto.setDescription(t.getDescription());
        dto.setCategory(t.getCategory());
        dto.setIsTaxDeductible(t.getIsTaxDeductible());
        dto.setIsIncome(t.getIsIncome());
        dto.setCategorizationConfidence(t.getCategorizationConfidence());
        dto.setReceiptUrl(t.getReceiptUrl());
        return dto;
    }
    
    @PostMapping("/sync")
    @Operation(summary = "Simulate Open Banking sync", 
               description = "Simulates syncing transactions from bank account")
    public ResponseEntity<String> syncTransactions() {
        return ResponseEntity.ok("Bank sync completed successfully. New transactions synced.");
    }
    
    @PutMapping("/{id}/category")
    @Operation(summary = "Update transaction category")
    public ResponseEntity<TransactionDTO> updateCategory(
            @PathVariable Long id,
            @RequestParam TransactionCategory category) {
        
        Transaction transaction = transactionService.getTransactionById(id);
        transaction.setCategory(category);
        transaction.setIsTaxDeductible(category.isTaxDeductible());
        Transaction updated = transactionService.saveTransaction(transaction);
        
        return ResponseEntity.ok(convertToDTO(updated));
    }
    
    @PostMapping("/{id}/receipt")
    @Operation(summary = "Upload receipt", description = "Upload receipt for transaction")
    public ResponseEntity<String> uploadReceipt(@PathVariable Long id) {
        // Mock implementation
        return ResponseEntity.ok("Receipt uploaded successfully");
    }
    
    @GetMapping("/{id}/receipt")
    @Operation(summary = "Download receipt PDF")
    public ResponseEntity<String> downloadReceipt(@PathVariable Long id) {
        // Mock implementation
        return ResponseEntity.ok("Receipt download URL: /receipts/" + id + ".pdf");
    }
}


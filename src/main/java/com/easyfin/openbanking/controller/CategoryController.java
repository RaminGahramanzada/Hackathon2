package com.easyfin.openbanking.controller;

import com.easyfin.openbanking.enums.TransactionCategory;
import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.model.Transaction;
import com.easyfin.openbanking.repository.BusinessRepository;
import com.easyfin.openbanking.repository.TransactionRepository;
import com.easyfin.openbanking.service.CategorizationService;
import com.easyfin.openbanking.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Category and spending categorization controller
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Spending categorization endpoints")
public class CategoryController {
    
    private final BusinessRepository businessRepository;
    private final TransactionRepository transactionRepository;
    private final CategorizationService categorizationService;
    private final TransactionService transactionService;
    
    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<List<Map<String, Object>>> getAllCategories() {
        List<Map<String, Object>> categories = Arrays.stream(TransactionCategory.values())
                .map(cat -> {
                    Map<String, Object> catMap = new HashMap<>();
                    catMap.put("name", cat.name());
                    catMap.put("displayName", cat.getDisplayName());
                    catMap.put("isTaxDeductible", cat.isTaxDeductible());
                    catMap.put("isIncome", cat.isIncome());
                    return catMap;
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/spending")
    @Operation(summary = "Get spending breakdown by category")
    public ResponseEntity<Map<String, BigDecimal>> getSpendingByCategory() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(30);
        
        List<Object[]> data = transactionRepository.sumExpensesByCategory(
                business.getId(), startDate, endDate);
        
        Map<String, BigDecimal> spending = new HashMap<>();
        for (Object[] row : data) {
            TransactionCategory category = (TransactionCategory) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            spending.put(category.getDisplayName(), amount);
        }
        
        return ResponseEntity.ok(spending);
    }
    
    @PostMapping("/{transactionId}/auto-categorize")
    @Operation(summary = "Auto-categorize transaction")
    public ResponseEntity<Map<String, Object>> autoCategorizeTransaction(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.categorizeTransaction(transactionId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", transaction.getId());
        response.put("category", transaction.getCategory());
        response.put("isTaxDeductible", transaction.getIsTaxDeductible());
        response.put("confidence", transaction.getCategorizationConfidence());
        response.put("message", "Transaction categorized successfully");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/tax-ready")
    @Operation(summary = "Get tax-ready expenses by category")
    public ResponseEntity<Map<String, BigDecimal>> getTaxReadyExpenses() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<Transaction> taxDeductible = transactionRepository.findByBusinessIdAndIsTaxDeductibleTrue(business.getId());
        
        Map<String, BigDecimal> taxReady = taxDeductible.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getDisplayName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));
        
        return ResponseEntity.ok(taxReady);
    }
}


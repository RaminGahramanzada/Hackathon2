package com.easyfin.openbanking.service;

import com.easyfin.openbanking.dto.TransactionDTO;
import com.easyfin.openbanking.model.Transaction;
import com.easyfin.openbanking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for transaction management
 */
@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final CategorizationService categorizationService;
    
    /**
     * Get all transactions for a business
     */
    public List<TransactionDTO> getAllTransactions(Long businessId) {
        return transactionRepository.findByBusinessIdOrderByTransactionDateDesc(businessId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get transactions by date range
     */
    public List<TransactionDTO> getTransactionsByDateRange(Long businessId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByBusinessIdAndDateRange(businessId, startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get transaction by ID
     */
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }
    
    /**
     * Save transaction
     */
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    
    /**
     * Auto-categorize transaction
     */
    public Transaction categorizeTransaction(Long transactionId) {
        Transaction transaction = getTransactionById(transactionId);
        categorizationService.categorizeTransaction(transaction);
        return transactionRepository.save(transaction);
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
}


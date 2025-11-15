package com.easyfin.openbanking.service;

import com.easyfin.openbanking.enums.TransactionCategory;
import com.easyfin.openbanking.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for smart transaction categorization
 */
@Service
public class CategorizationService {
    
    private static final Map<String, TransactionCategory> MERCHANT_KEYWORDS = new HashMap<>();
    
    static {
        // Food suppliers and ingredients
        MERCHANT_KEYWORDS.put("bazar", TransactionCategory.FOOD_SUPPLIES);
        MERCHANT_KEYWORDS.put("market", TransactionCategory.FOOD_SUPPLIES);
        MERCHANT_KEYWORDS.put("food", TransactionCategory.FOOD_SUPPLIES);
        MERCHANT_KEYWORDS.put("meat", TransactionCategory.FOOD_SUPPLIES);
        MERCHANT_KEYWORDS.put("vegetable", TransactionCategory.FOOD_SUPPLIES);
        MERCHANT_KEYWORDS.put("supplier", TransactionCategory.FOOD_SUPPLIES);
        
        // Utilities
        MERCHANT_KEYWORDS.put("azercell", TransactionCategory.TELECOMMUNICATIONS);
        MERCHANT_KEYWORDS.put("bakcell", TransactionCategory.TELECOMMUNICATIONS);
        MERCHANT_KEYWORDS.put("nar", TransactionCategory.TELECOMMUNICATIONS);
        MERCHANT_KEYWORDS.put("azersu", TransactionCategory.UTILITIES);
        MERCHANT_KEYWORDS.put("azerishiq", TransactionCategory.UTILITIES);
        MERCHANT_KEYWORDS.put("azergas", TransactionCategory.UTILITIES);
        MERCHANT_KEYWORDS.put("water", TransactionCategory.UTILITIES);
        MERCHANT_KEYWORDS.put("electricity", TransactionCategory.UTILITIES);
        MERCHANT_KEYWORDS.put("gas", TransactionCategory.UTILITIES);
        
        // Travel and transport
        MERCHANT_KEYWORDS.put("bolt", TransactionCategory.TRAVEL);
        MERCHANT_KEYWORDS.put("uber", TransactionCategory.TRAVEL);
        MERCHANT_KEYWORDS.put("taxi", TransactionCategory.TRAVEL);
        MERCHANT_KEYWORDS.put("fuel", TransactionCategory.TRAVEL);
        MERCHANT_KEYWORDS.put("petrol", TransactionCategory.TRAVEL);
        
        // Technology and software
        MERCHANT_KEYWORDS.put("aws", TransactionCategory.SOFTWARE);
        MERCHANT_KEYWORDS.put("microsoft", TransactionCategory.SOFTWARE);
        MERCHANT_KEYWORDS.put("software", TransactionCategory.SOFTWARE);
        MERCHANT_KEYWORDS.put("hosting", TransactionCategory.SOFTWARE);
        MERCHANT_KEYWORDS.put("domain", TransactionCategory.SOFTWARE);
        
        // Maintenance and equipment
        MERCHANT_KEYWORDS.put("repair", TransactionCategory.MAINTENANCE);
        MERCHANT_KEYWORDS.put("maintenance", TransactionCategory.MAINTENANCE);
        MERCHANT_KEYWORDS.put("equipment", TransactionCategory.EQUIPMENT);
        MERCHANT_KEYWORDS.put("kitchen", TransactionCategory.EQUIPMENT);
        
        // Marketing
        MERCHANT_KEYWORDS.put("facebook", TransactionCategory.MARKETING);
        MERCHANT_KEYWORDS.put("google ads", TransactionCategory.MARKETING);
        MERCHANT_KEYWORDS.put("instagram", TransactionCategory.MARKETING);
        MERCHANT_KEYWORDS.put("advertising", TransactionCategory.MARKETING);
        
        // Rent
        MERCHANT_KEYWORDS.put("rent", TransactionCategory.RENT);
        MERCHANT_KEYWORDS.put("lease", TransactionCategory.RENT);
        
        // Office supplies
        MERCHANT_KEYWORDS.put("office", TransactionCategory.OFFICE_SUPPLIES);
        MERCHANT_KEYWORDS.put("stationery", TransactionCategory.OFFICE_SUPPLIES);
        MERCHANT_KEYWORDS.put("paper", TransactionCategory.OFFICE_SUPPLIES);
    }
    
    /**
     * Auto-categorize transaction based on merchant name and description
     */
    public void categorizeTransaction(Transaction transaction) {
        String searchText = (transaction.getMerchantName() + " " + 
                            (transaction.getDescription() != null ? transaction.getDescription() : ""))
                            .toLowerCase();
        
        TransactionCategory detectedCategory = TransactionCategory.UNCATEGORIZED;
        double confidence = 0.0;
        
        // Check for keyword matches
        for (Map.Entry<String, TransactionCategory> entry : MERCHANT_KEYWORDS.entrySet()) {
            if (searchText.contains(entry.getKey())) {
                detectedCategory = entry.getValue();
                confidence = 0.85; // High confidence for keyword match
                break;
            }
        }
        
        // If no keyword match, use partial matching for lower confidence
        if (detectedCategory == TransactionCategory.UNCATEGORIZED) {
            confidence = 0.50; // Lower confidence for fallback
        }
        
        transaction.setCategory(detectedCategory);
        transaction.setCategorizationConfidence(confidence);
        transaction.setIsTaxDeductible(detectedCategory.isTaxDeductible());
    }
    
    /**
     * Get categorization confidence score
     */
    public double getConfidenceScore(String merchantName, TransactionCategory category) {
        String searchText = merchantName.toLowerCase();
        
        for (Map.Entry<String, TransactionCategory> entry : MERCHANT_KEYWORDS.entrySet()) {
            if (searchText.contains(entry.getKey()) && entry.getValue() == category) {
                return 0.85;
            }
        }
        
        return 0.50;
    }
}


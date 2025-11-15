package com.easyfin.openbanking.dto;

import com.easyfin.openbanking.enums.TransactionCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private LocalDateTime transactionDate;
    private BigDecimal amount;
    private String currency;
    private String merchantName;
    private String description;
    private TransactionCategory category;
    private Boolean isTaxDeductible;
    private Boolean isIncome;
    private Double categorizationConfidence;
    private String receiptUrl;
}


package com.easyfin.openbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private BigDecimal potentialSavings;
    private String priority;
    private Boolean isActedUpon;
    private LocalDateTime createdAt;
}


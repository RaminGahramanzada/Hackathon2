package com.easyfin.openbanking.dto;

import com.easyfin.openbanking.enums.AlertType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Alert notification for important events and warnings")
public class AlertDTO {
    
    @Schema(description = "Unique alert identifier", example = "1")
    private Long id;
    
    @Schema(description = "Type of alert", example = "TAX_DEADLINE", 
            allowableValues = {"LOW_BALANCE", "TAX_DEADLINE", "UNUSUAL_SPENDING", "UNAUTHORIZED_PAYMENT", "RECOMMENDATION"})
    private AlertType alertType;
    
    @Schema(description = "Alert title", example = "Tax Deadline Approaching")
    private String title;
    
    @Schema(description = "Detailed alert message", example = "VAT Payment in 5 days")
    private String message;
    
    @Schema(description = "Alert severity level", example = "high", 
            allowableValues = {"low", "medium", "high", "critical"})
    private String severity;
    
    @Schema(description = "Whether alert has been read by user", example = "false")
    private Boolean isRead;
    
    @Schema(description = "Whether alert has been dismissed by user", example = "false")
    private Boolean isDismissed;
    
    @Schema(description = "When the alert was created", example = "2024-11-14T10:00:00")
    private LocalDateTime createdAt;
    
    // Additional fields for tax deadlines
    @Schema(description = "Tax deadline date (for TAX_DEADLINE alerts)", example = "2024-12-20")
    private LocalDate deadlineDate;
    
    @Schema(description = "Days remaining until deadline (for TAX_DEADLINE alerts)", example = "5")
    private Integer daysRemaining;
    
    @Schema(description = "Action URL or deep link for the alert", example = "/taxes/declarations")
    private String actionUrl;
}


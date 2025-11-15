package com.easyfin.openbanking.service;

import com.easyfin.openbanking.dto.AlertDTO;
import com.easyfin.openbanking.model.Alert;
import com.easyfin.openbanking.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for intelligent alerts
 */
@Service
@RequiredArgsConstructor
public class AlertService {
    
    private final AlertRepository alertRepository;
    
    /**
     * Get all active alerts
     */
    public List<AlertDTO> getActiveAlerts(Long businessId) {
        return alertRepository.findByBusinessIdAndIsDismissedFalseOrderByCreatedAtDesc(businessId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get alerts by type
     */
    public List<AlertDTO> getAlertsByType(Long businessId, String alertType) {
        return alertRepository.findByBusinessIdAndIsDismissedFalseOrderByCreatedAtDesc(businessId)
                .stream()
                .filter(a -> a.getAlertType().name().equals(alertType))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Dismiss alert
     */
    public void dismissAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        alert.setIsDismissed(true);
        alert.setDismissedAt(LocalDateTime.now());
        alertRepository.save(alert);
    }
    
    /**
     * Create alert
     */
    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }
    
    private AlertDTO convertToDTO(Alert a) {
        AlertDTO dto = new AlertDTO();
        dto.setId(a.getId());
        dto.setAlertType(a.getAlertType());
        dto.setTitle(a.getTitle());
        dto.setMessage(a.getMessage());
        dto.setSeverity(a.getSeverity());
        dto.setIsRead(a.getIsRead());
        dto.setIsDismissed(a.getIsDismissed());
        dto.setCreatedAt(a.getCreatedAt());
        
        // Extract additional info from metadata if available
        if (a.getMetadata() != null && !a.getMetadata().isEmpty()) {
            // Parse metadata for deadline date and days remaining
            if (a.getMetadata().containsKey("deadlineDate")) {
                dto.setDeadlineDate(java.time.LocalDate.parse(a.getMetadata().get("deadlineDate").toString()));
            }
            if (a.getMetadata().containsKey("daysRemaining")) {
                dto.setDaysRemaining(Integer.parseInt(a.getMetadata().get("daysRemaining").toString()));
            }
            if (a.getMetadata().containsKey("actionUrl")) {
                dto.setActionUrl(a.getMetadata().get("actionUrl").toString());
            }
        }
        
        return dto;
    }
}


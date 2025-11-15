package com.easyfin.openbanking.repository;

import com.easyfin.openbanking.enums.AlertType;
import com.easyfin.openbanking.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Alert entity
 */
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    
    List<Alert> findByBusinessId(Long businessId);
    
    List<Alert> findByBusinessIdOrderByCreatedAtDesc(Long businessId);
    
    List<Alert> findByBusinessIdAndIsDismissedFalse(Long businessId);
    
    List<Alert> findByBusinessIdAndIsDismissedFalseOrderByCreatedAtDesc(Long businessId);
    
    List<Alert> findByBusinessIdAndAlertType(Long businessId, AlertType alertType);
    
    List<Alert> findByBusinessIdAndSeverityAndIsDismissedFalse(Long businessId, String severity);
    
    Long countByBusinessIdAndIsDismissedFalse(Long businessId);
}


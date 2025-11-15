package com.easyfin.openbanking.model;

import com.easyfin.openbanking.enums.AlertType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Alert entity for intelligent notifications
 */
@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType alertType;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String message;
    
    @Column(nullable = false)
    private String severity; // high, medium, low
    
    @Column(columnDefinition = "boolean default false")
    private Boolean isRead = false;
    
    @Column(columnDefinition = "boolean default false")
    private Boolean isDismissed = false;
    
    private LocalDateTime dismissedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction relatedTransaction;
    
    // Metadata for additional alert information (e.g., deadline dates, days remaining, action URLs)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "varchar(1000)")
    private Map<String, Object> metadata = new HashMap<>();
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (metadata == null) {
            metadata = new HashMap<>();
        }
    }
}


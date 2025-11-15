package com.easyfin.openbanking.repository;

import com.easyfin.openbanking.enums.TaxStatus;
import com.easyfin.openbanking.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Business entity
 */
@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    
    Optional<Business> findByBusinessName(String businessName);
    
    List<Business> findByTaxStatus(TaxStatus taxStatus);
    
    List<Business> findByIsActiveTrue();
    
    Optional<Business> findFirstByIsActiveTrueOrderByCreatedAtDesc();
}


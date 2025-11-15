package com.easyfin.openbanking.repository;

import com.easyfin.openbanking.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Recommendation entity
 */
@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    
    List<Recommendation> findByBusinessId(Long businessId);
    
    List<Recommendation> findByBusinessIdOrderByCreatedAtDesc(Long businessId);
    
    List<Recommendation> findByBusinessIdAndIsActedUponFalse(Long businessId);
    
    List<Recommendation> findByBusinessIdAndCategory(Long businessId, String category);
    
    List<Recommendation> findByBusinessIdAndCategoryAndIsActedUponFalse(Long businessId, String category);
    
    List<Recommendation> findByBusinessIdAndPriority(Long businessId, String priority);
}


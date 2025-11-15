package com.easyfin.openbanking.controller;

import com.easyfin.openbanking.dto.RecommendationDTO;
import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.model.Recommendation;
import com.easyfin.openbanking.repository.BusinessRepository;
import com.easyfin.openbanking.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Actionable recommendations controller
 */
@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendations", description = "Actionable recommendation endpoints")
public class RecommendationController {
    
    private final RecommendationService recommendationService;
    private final BusinessRepository businessRepository;
    
    @GetMapping
    @Operation(summary = "Get all smart recommendations")
    public ResponseEntity<List<RecommendationDTO>> getAllRecommendations() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<Recommendation> recommendations = recommendationService.getActiveRecommendations(business.getId());
        return ResponseEntity.ok(recommendations.stream().map(this::convertToDTO).collect(Collectors.toList()));
    }
    
    private RecommendationDTO convertToDTO(Recommendation r) {
        RecommendationDTO dto = new RecommendationDTO();
        dto.setId(r.getId());
        dto.setTitle(r.getTitle());
        dto.setDescription(r.getDescription());
        dto.setCategory(r.getCategory());
        dto.setPotentialSavings(r.getPotentialSavings());
        dto.setPriority(r.getPriority());
        dto.setIsActedUpon(r.getIsActedUpon());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
    
    @GetMapping("/optimize-spending")
    @Operation(summary = "Get spending optimization suggestions")
    public ResponseEntity<List<RecommendationDTO>> getSpendingOptimizations() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<Recommendation> recommendations = recommendationService.getActiveRecommendations(business.getId());
        return ResponseEntity.ok(recommendations.stream()
                .filter(r -> r.getCategory().equals("cost-reduction"))
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }
    
    @GetMapping("/reduce-costs")
    @Operation(summary = "Get cost reduction opportunities")
    public ResponseEntity<List<RecommendationDTO>> getCostReductions() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<Recommendation> recommendations = recommendationService.getActiveRecommendations(business.getId());
        return ResponseEntity.ok(recommendations.stream()
                .filter(r -> r.getCategory().equals("cost-reduction"))
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }
    
    @GetMapping("/cash-shortage-warnings")
    @Operation(summary = "Get cash shortage predictions")
    public ResponseEntity<List<RecommendationDTO>> getCashShortageWarnings() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<Recommendation> recommendations = recommendationService.getActiveRecommendations(business.getId());
        return ResponseEntity.ok(recommendations.stream()
                .filter(r -> r.getCategory().equals("cash-flow"))
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }
    
    @GetMapping("/tax-savings")
    @Operation(summary = "Get tax saving opportunities")
    public ResponseEntity<List<RecommendationDTO>> getTaxSavingsRecommendations() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<Recommendation> recommendations = recommendationService.getActiveRecommendations(business.getId());
        return ResponseEntity.ok(recommendations.stream()
                .filter(r -> r.getCategory().equals("tax-savings"))
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
    }
    
    @PostMapping("/{id}/action")
    @Operation(summary = "Mark recommendation as acted upon")
    public ResponseEntity<String> markAsActedUpon(@PathVariable Long id) {
        // Mock implementation - in real app, fetch and update
        return ResponseEntity.ok("Recommendation marked as acted upon");
    }
}


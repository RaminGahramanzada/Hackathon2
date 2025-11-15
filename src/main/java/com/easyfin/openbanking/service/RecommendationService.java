package com.easyfin.openbanking.service;

import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.model.Recommendation;
import com.easyfin.openbanking.repository.RecommendationRepository;
import com.easyfin.openbanking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for generating actionable recommendations
 */
@Service
@RequiredArgsConstructor
public class RecommendationService {
    
    private final RecommendationRepository recommendationRepository;
    private final TransactionRepository transactionRepository;
    
    /**
     * Generate recommendations for a business
     */
    public List<Recommendation> generateRecommendations(Business business) {
        List<Recommendation> recommendations = new ArrayList<>();
        
        // Tax savings recommendation
        if (business.getTaxExemption() != null && business.getTaxExemption() > 0) {
            Recommendation taxRec = new Recommendation();
            taxRec.setBusiness(business);
            taxRec.setTitle("You qualify for 75% tax exemption");
            taxRec.setDescription(String.format(
                    "As a micro-entrepreneur with %d employees and income under 200,000 AZN, " +
                    "you qualify for 75%% income tax exemption. Estimated annual savings: %.2f AZN",
                    business.getEmployeeCount(),
                    business.getAnnualIncome().multiply(new BigDecimal("0.15")).doubleValue()
            ));
            taxRec.setCategory("tax-savings");
            taxRec.setPotentialSavings(business.getAnnualIncome().multiply(new BigDecimal("0.15")));
            taxRec.setPriority("high");
            recommendations.add(taxRec);
        }
        
        // Cash flow warning
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(30);
        BigDecimal expenses = transactionRepository.sumExpensesByBusinessIdAndDateRange(
                business.getId(), startDate, endDate);
        
        if (expenses != null && expenses.compareTo(new BigDecimal("15000")) > 0) {
            Recommendation cashRec = new Recommendation();
            cashRec.setBusiness(business);
            cashRec.setTitle("High monthly expenses detected");
            cashRec.setDescription(String.format(
                    "Your expenses in the last 30 days: %.2f AZN. Review your spending to identify " +
                    "potential cost reduction opportunities.",
                    expenses.doubleValue()
            ));
            cashRec.setCategory("cost-reduction");
            cashRec.setPotentialSavings(expenses.multiply(new BigDecimal("0.10"))); // 10% potential savings
            cashRec.setPriority("medium");
            recommendations.add(cashRec);
        }
        
        return recommendationRepository.saveAll(recommendations);
    }
    
    /**
     * Get active recommendations
     */
    public List<Recommendation> getActiveRecommendations(Long businessId) {
        return recommendationRepository.findByBusinessIdAndIsActedUponFalse(businessId);
    }
}


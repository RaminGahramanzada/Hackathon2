package com.easyfin.openbanking.controller;

import com.easyfin.openbanking.dto.DashboardDTO;
import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.repository.BusinessRepository;
import com.easyfin.openbanking.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Dashboard controller for overview and summary data
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard and overview endpoints")
public class DashboardController {
    
    private final DashboardService dashboardService;
    private final BusinessRepository businessRepository;
    
    @GetMapping("/summary")
    @Operation(
        summary = "Get dashboard summary for home screen", 
        description = """
            Returns complete dashboard data including:
            - User profile (firstName, lastName, fullName)
            - Balance information (total, available, pending)
            - Financial metrics (income, expenses, cash flow)
            - Summary counts (employees, alerts, recommendations)
            - Recent transactions and spending by category
            
            Balance Fields:
            - totalBalance: Total money in account (income - expenses)
            - availableBalance: Current usable money (Hazirda hesabda olan pul)
            - pendingBalance: Money in tax processing, available in ~2 weeks (Vergide qalan pul)
            """
    )
    public ResponseEntity<DashboardDTO> getDashboardSummary() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        DashboardDTO dashboard = dashboardService.getDashboardSummary(business);
        return ResponseEntity.ok(dashboard);
    }
}


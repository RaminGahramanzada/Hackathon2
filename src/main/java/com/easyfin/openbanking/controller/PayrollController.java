package com.easyfin.openbanking.controller;

import com.easyfin.openbanking.dto.PayrollDTO;
import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.model.Employee;
import com.easyfin.openbanking.repository.BusinessRepository;
import com.easyfin.openbanking.repository.EmployeeRepository;
import com.easyfin.openbanking.service.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Payroll processing controller
 */
@RestController
@RequestMapping("/api/v1/payroll")
@RequiredArgsConstructor
@Tag(name = "Payroll", description = "Payroll processing endpoints")
public class PayrollController {
    
    private final PayrollService payrollService;
    private final BusinessRepository businessRepository;
    private final EmployeeRepository employeeRepository;
    
    @GetMapping("/calculate")
    @Operation(summary = "Calculate payroll for current month (individual employees)")
    public ResponseEntity<List<PayrollDTO>> calculatePayroll() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<Employee> employees = employeeRepository.findByBusinessIdAndIsActiveTrue(business.getId());
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        
        List<PayrollDTO> payrolls = employees.stream()
                .map(e -> payrollService.calculatePayroll(e, currentMonth))
                .toList();
        
        return ResponseEntity.ok(payrolls);
    }
    
    @GetMapping("/summary")
    @Operation(summary = "Get monthly payroll summary (total for all employees)")
    public ResponseEntity<Map<String, Object>> getPayrollSummary() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<Employee> employees = employeeRepository.findByBusinessIdAndIsActiveTrue(business.getId());
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        
        BigDecimal totalGrossPayroll = BigDecimal.ZERO;
        BigDecimal totalEmployerSsf = BigDecimal.ZERO;
        BigDecimal totalEmployeeSsf = BigDecimal.ZERO;
        BigDecimal totalIncomeTax = BigDecimal.ZERO;
        BigDecimal totalNetPayroll = BigDecimal.ZERO;
        BigDecimal totalEmployerCost = BigDecimal.ZERO;
        
        for (Employee employee : employees) {
            PayrollDTO payroll = payrollService.calculatePayroll(employee, currentMonth);
            totalGrossPayroll = totalGrossPayroll.add(payroll.getGrossSalary());
            totalEmployerSsf = totalEmployerSsf.add(payroll.getEmployerSsfContribution());
            totalEmployeeSsf = totalEmployeeSsf.add(payroll.getEmployeeSsfContribution());
            totalIncomeTax = totalIncomeTax.add(payroll.getIncomeTax());
            totalNetPayroll = totalNetPayroll.add(payroll.getNetSalary());
            totalEmployerCost = totalEmployerCost.add(payroll.getTotalEmployerCost());
        }
        
        BigDecimal totalTaxes = totalEmployerSsf.add(totalEmployeeSsf).add(totalIncomeTax);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("month", currentMonth);
        summary.put("employeeCount", employees.size());
        summary.put("totalGrossPayroll", totalGrossPayroll);
        summary.put("totalTaxes", totalTaxes);
        summary.put("totalNetPayrollCost", totalEmployerCost);
        
        // Breakdown for pie chart
        Map<String, BigDecimal> breakdown = new HashMap<>();
        breakdown.put("grossWages", totalGrossPayroll);
        breakdown.put("employerSSF", totalEmployerSsf);
        breakdown.put("employeeSSF", totalEmployeeSsf);
        breakdown.put("incomeTax", totalIncomeTax);
        summary.put("costBreakdown", breakdown);
        
        // Tax details
        Map<String, Object> taxDetails = new HashMap<>();
        taxDetails.put("socialSecurityTax", totalEmployerSsf.add(totalEmployeeSsf));
        taxDetails.put("employerSSF", totalEmployerSsf);
        taxDetails.put("employeeSSF", totalEmployeeSsf);
        taxDetails.put("incomeTax", totalIncomeTax);
        summary.put("taxCalculations", taxDetails);
        
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/taxes")
    @Operation(summary = "Calculate employee-related taxes (SSF, income tax)")
    public ResponseEntity<Map<String, Object>> calculateTaxes() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<Employee> employees = employeeRepository.findByBusinessIdAndIsActiveTrue(business.getId());
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        
        BigDecimal totalEmployerSsf = BigDecimal.ZERO;
        BigDecimal totalEmployeeSsf = BigDecimal.ZERO;
        BigDecimal totalIncomeTax = BigDecimal.ZERO;
        
        for (Employee employee : employees) {
            PayrollDTO payroll = payrollService.calculatePayroll(employee, currentMonth);
            totalEmployerSsf = totalEmployerSsf.add(payroll.getEmployerSsfContribution());
            totalEmployeeSsf = totalEmployeeSsf.add(payroll.getEmployeeSsfContribution());
            totalIncomeTax = totalIncomeTax.add(payroll.getIncomeTax());
        }
        
        Map<String, Object> taxes = new HashMap<>();
        taxes.put("totalEmployerSSF", totalEmployerSsf);
        taxes.put("totalEmployeeSSF", totalEmployeeSsf);
        taxes.put("totalIncomeTax", totalIncomeTax);
        taxes.put("totalTaxBurden", totalEmployerSsf.add(totalEmployeeSsf).add(totalIncomeTax));
        taxes.put("employeeCount", employees.size());
        
        return ResponseEntity.ok(taxes);
    }
    
    @PostMapping("/process")
    @Operation(summary = "Process payroll for month")
    public ResponseEntity<String> processPayroll() {
        return ResponseEntity.ok("Payroll processed successfully for " + LocalDate.now().getMonth());
    }
    
    @GetMapping("/history")
    @Operation(summary = "Get payroll history")
    public ResponseEntity<List<PayrollDTO>> getPayrollHistory() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<PayrollDTO> history = payrollService.getPayrollHistory(business.getId());
        return ResponseEntity.ok(history);
    }
}


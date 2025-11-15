package com.easyfin.openbanking.service;

import com.easyfin.openbanking.dto.PayrollDTO;
import com.easyfin.openbanking.model.Employee;
import com.easyfin.openbanking.model.Payroll;
import com.easyfin.openbanking.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for payroll processing
 */
@Service
@RequiredArgsConstructor
public class PayrollService {
    
    private final PayrollRepository payrollRepository;
    
    private static final BigDecimal EMPLOYEE_SSF_RATE = new BigDecimal("0.03"); // 3%
    private static final BigDecimal EMPLOYER_SSF_RATE = new BigDecimal("0.22"); // 22%
    private static final BigDecimal INCOME_TAX_RATE = new BigDecimal("0.14"); // 14% (simplified)
    
    /**
     * Calculate payroll for an employee
     */
    public PayrollDTO calculatePayroll(Employee employee, LocalDate payrollMonth) {
        BigDecimal grossSalary = employee.getMonthlySalary();
        
        // Calculate SSF contributions
        BigDecimal employeeSsf = grossSalary.multiply(EMPLOYEE_SSF_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal employerSsf = grossSalary.multiply(EMPLOYER_SSF_RATE).setScale(2, RoundingMode.HALF_UP);
        
        // Calculate income tax (on gross salary minus employee SSF)
        BigDecimal taxableIncome = grossSalary.subtract(employeeSsf);
        BigDecimal incomeTax = taxableIncome.multiply(INCOME_TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        
        // Calculate net salary
        BigDecimal netSalary = grossSalary.subtract(employeeSsf).subtract(incomeTax);
        
        // Total cost to employer
        BigDecimal totalEmployerCost = grossSalary.add(employerSsf);
        
        PayrollDTO dto = new PayrollDTO();
        dto.setEmployeeId(employee.getId());
        dto.setEmployeeName(employee.getFullName());
        dto.setPayrollMonth(payrollMonth);
        dto.setGrossSalary(grossSalary);
        dto.setEmployeeSsfContribution(employeeSsf);
        dto.setEmployerSsfContribution(employerSsf);
        dto.setIncomeTax(incomeTax);
        dto.setNetSalary(netSalary);
        dto.setTotalEmployerCost(totalEmployerCost);
        dto.setIsPaid(false);
        
        return dto;
    }
    
    /**
     * Process payroll for month
     */
    public Payroll processPayroll(Payroll payroll) {
        return payrollRepository.save(payroll);
    }
    
    /**
     * Get payroll history
     */
    public List<PayrollDTO> getPayrollHistory(Long businessId) {
        return payrollRepository.findByBusinessIdOrderByPayrollMonthDesc(businessId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private PayrollDTO convertToDTO(Payroll payroll) {
        PayrollDTO dto = new PayrollDTO();
        dto.setId(payroll.getId());
        dto.setEmployeeId(payroll.getEmployee().getId());
        dto.setEmployeeName(payroll.getEmployee().getFullName());
        dto.setPayrollMonth(payroll.getPayrollMonth());
        dto.setGrossSalary(payroll.getGrossSalary());
        dto.setEmployeeSsfContribution(payroll.getEmployeeSsfContribution());
        dto.setEmployerSsfContribution(payroll.getEmployerSsfContribution());
        dto.setIncomeTax(payroll.getIncomeTax());
        dto.setNetSalary(payroll.getNetSalary());
        dto.setTotalEmployerCost(payroll.getTotalEmployerCost());
        dto.setIsPaid(payroll.getIsPaid());
        dto.setPaidDate(payroll.getPaidDate());
        return dto;
    }
}


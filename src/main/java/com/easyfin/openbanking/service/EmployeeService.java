package com.easyfin.openbanking.service;

import com.easyfin.openbanking.dto.EmployeeDTO;
import com.easyfin.openbanking.model.Employee;
import com.easyfin.openbanking.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for employee management
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    
    /**
     * Get all employees for a business
     */
    public List<EmployeeDTO> getAllEmployees(Long businessId) {
        return employeeRepository.findByBusinessIdAndIsActiveTrue(businessId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get employee by ID
     */
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }
    
    /**
     * Save employee
     */
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    /**
     * Delete employee (soft delete)
     */
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employee.setIsActive(false);
        employeeRepository.save(employee);
    }
    
    private EmployeeDTO convertToDTO(Employee e) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(e.getId());
        dto.setFirstName(e.getFirstName());
        dto.setLastName(e.getLastName());
        dto.setFullName(e.getFullName());
        dto.setPosition(e.getPosition());
        dto.setMonthlySalary(e.getMonthlySalary());
        dto.setHireDate(e.getHireDate());
        dto.setEmail(e.getEmail());
        dto.setPhone(e.getPhone());
        dto.setIsActive(e.getIsActive());
        return dto;
    }
}


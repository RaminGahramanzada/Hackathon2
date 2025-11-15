package com.easyfin.openbanking.controller;

import com.easyfin.openbanking.dto.EmployeeDTO;
import com.easyfin.openbanking.model.Business;
import com.easyfin.openbanking.model.Employee;
import com.easyfin.openbanking.repository.BusinessRepository;
import com.easyfin.openbanking.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Employee management controller
 */
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Employee management endpoints")
public class EmployeeController {
    
    private final EmployeeService employeeService;
    private final BusinessRepository businessRepository;
    
    @GetMapping
    @Operation(summary = "Get all employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        List<EmployeeDTO> employees = employeeService.getAllEmployees(business.getId());
        return ResponseEntity.ok(employees);
    }
    
    @PostMapping
    @Operation(summary = "Add new employee")
    public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody EmployeeDTO employeeDto) {
        Business business = businessRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No active business found"));
        
        Employee employee = new Employee();
        employee.setBusiness(business);
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setPosition(employeeDto.getPosition());
        employee.setMonthlySalary(employeeDto.getMonthlySalary());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhone(employeeDto.getPhone());
        employee.setIsActive(true);
        
        Employee saved = employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update employee info")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDto) {
        Employee existing = employeeService.getEmployeeById(id);
        existing.setFirstName(employeeDto.getFirstName());
        existing.setLastName(employeeDto.getLastName());
        existing.setPosition(employeeDto.getPosition());
        existing.setMonthlySalary(employeeDto.getMonthlySalary());
        existing.setEmail(employeeDto.getEmail());
        existing.setPhone(employeeDto.getPhone());
        
        Employee updated = employeeService.saveEmployee(existing);
        return ResponseEntity.ok(convertToDTO(updated));
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
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove employee (soft delete)")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deactivated successfully");
    }
}


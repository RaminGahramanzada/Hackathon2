package com.easyfin.openbanking.repository;

import com.easyfin.openbanking.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Employee entity
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    List<Employee> findByBusinessId(Long businessId);
    
    List<Employee> findByBusinessIdAndIsActiveTrue(Long businessId);
    
    Long countByBusinessIdAndIsActiveTrue(Long businessId);
    
    List<Employee> findByIsActiveTrueOrderByMonthlySalaryDesc();
}


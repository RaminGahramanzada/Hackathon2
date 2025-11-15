package com.easyfin.openbanking.repository;

import com.easyfin.openbanking.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Payroll entity
 */
@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    
    List<Payroll> findByBusinessId(Long businessId);
    
    List<Payroll> findByBusinessIdOrderByPayrollMonthDesc(Long businessId);
    
    List<Payroll> findByEmployeeId(Long employeeId);
    
    Optional<Payroll> findByEmployeeIdAndPayrollMonth(Long employeeId, LocalDate payrollMonth);
    
    List<Payroll> findByBusinessIdAndPayrollMonth(Long businessId, LocalDate payrollMonth);
    
    List<Payroll> findByBusinessIdAndIsPaidFalse(Long businessId);
    
    @Query("SELECT SUM(p.totalEmployerCost) FROM Payroll p " +
           "WHERE p.business.id = :businessId " +
           "AND p.payrollMonth >= :startDate " +
           "AND p.payrollMonth <= :endDate")
    BigDecimal sumTotalPayrollCostByBusinessAndDateRange(@Param("businessId") Long businessId,
                                                          @Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);
}


package com.easyfin.openbanking.repository;

import com.easyfin.openbanking.enums.TransactionCategory;
import com.easyfin.openbanking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Transaction entity
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByBusinessId(Long businessId);
    
    List<Transaction> findByBusinessIdOrderByTransactionDateDesc(Long businessId);
    
    List<Transaction> findByBusinessIdAndCategory(Long businessId, TransactionCategory category);
    
    List<Transaction> findByBusinessIdAndIsTaxDeductibleTrue(Long businessId);
    
    List<Transaction> findByBusinessIdAndCategoryOrderByTransactionDateDesc(Long businessId, TransactionCategory category);
    
    List<Transaction> findByBusinessIdAndTransactionDateBetween(Long businessId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT t FROM Transaction t WHERE t.business.id = :businessId " +
           "AND t.transactionDate >= :startDate " +
           "AND t.transactionDate <= :endDate " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findByBusinessIdAndDateRange(@Param("businessId") Long businessId,
                                                     @Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.business.id = :businessId " +
           "AND t.isIncome = true " +
           "AND t.transactionDate >= :startDate " +
           "AND t.transactionDate <= :endDate")
    BigDecimal sumIncomeByBusinessIdAndDateRange(@Param("businessId") Long businessId,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.business.id = :businessId " +
           "AND t.isIncome = false " +
           "AND t.transactionDate >= :startDate " +
           "AND t.transactionDate <= :endDate")
    BigDecimal sumExpensesByBusinessIdAndDateRange(@Param("businessId") Long businessId,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.business.id = :businessId " +
           "AND t.isTaxDeductible = true " +
           "AND t.transactionDate >= :startDate " +
           "AND t.transactionDate <= :endDate")
    BigDecimal sumTaxDeductibleByBusinessIdAndDateRange(@Param("businessId") Long businessId,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t " +
           "WHERE t.business.id = :businessId " +
           "AND t.isIncome = false " +
           "AND t.transactionDate >= :startDate " +
           "AND t.transactionDate <= :endDate " +
           "GROUP BY t.category")
    List<Object[]> sumExpensesByCategory(@Param("businessId") Long businessId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);
    
    // Find income transactions by date range
    List<Transaction> findByBusinessIdAndIsIncomeTrueAndTransactionDateBetween(
            Long businessId, 
            LocalDateTime startDate, 
            LocalDateTime endDate);
    
    // Find expense transactions by date range
    List<Transaction> findByBusinessIdAndIsIncomeFalseAndTransactionDateBetween(
            Long businessId, 
            LocalDateTime startDate, 
            LocalDateTime endDate);
}


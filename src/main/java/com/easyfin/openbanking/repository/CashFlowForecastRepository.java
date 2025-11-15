package com.easyfin.openbanking.repository;

import com.easyfin.openbanking.model.CashFlowForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for CashFlowForecast entity
 */
@Repository
public interface CashFlowForecastRepository extends JpaRepository<CashFlowForecast, Long> {
    
    List<CashFlowForecast> findByBusinessId(Long businessId);
    
    List<CashFlowForecast> findByBusinessIdOrderByForecastDateAsc(Long businessId);
    
    List<CashFlowForecast> findByBusinessIdAndForecastDateBetween(Long businessId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT c FROM CashFlowForecast c WHERE c.business.id = :businessId " +
           "AND c.forecastDate >= :startDate " +
           "ORDER BY c.forecastDate ASC")
    List<CashFlowForecast> findFutureForecasts(@Param("businessId") Long businessId,
                                                @Param("startDate") LocalDate startDate);
}


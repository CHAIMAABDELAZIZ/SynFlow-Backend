package com.example.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.model.DailyReport;
import com.example.backend.model.Puit;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
    List<DailyReport> findByConcernedWell(Puit puit);
    List<DailyReport> findByReportDate(LocalDate reportDate);
    List<DailyReport> findByConcernedWellAndReportDate(Puit puit, LocalDate reportDate);
    
    @Query("SELECT DISTINCT dr FROM DailyReport dr " +
           "LEFT JOIN FETCH dr.operationsPerformed " +
           "LEFT JOIN FETCH dr.indicators " +
           "WHERE dr.id = :id")
    Optional<DailyReport> findByIdWithCollections(@Param("id") Long id);
    
    @Query("SELECT DISTINCT dr FROM DailyReport dr " +
           "LEFT JOIN FETCH dr.operationsPerformed " +
           "LEFT JOIN FETCH dr.indicators")
    List<DailyReport> findAllWithCollections();
    
    @Query("SELECT DISTINCT dr FROM DailyReport dr " +
           "LEFT JOIN FETCH dr.operationsPerformed " +
           "LEFT JOIN FETCH dr.indicators " +
           "WHERE dr.concernedWell = :puit")
    List<DailyReport> findByConcernedWellWithCollections(@Param("puit") Puit puit);
}

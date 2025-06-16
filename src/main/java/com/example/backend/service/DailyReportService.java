package com.example.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.DailyReport;
import com.example.backend.repository.DailyReportRepository;
import com.example.backend.repository.PhaseRepository;
import com.example.backend.repository.PuitRepository;

@Service
@Transactional
public class DailyReportService {

    @Autowired
    private DailyReportRepository dailyReportRepository;
    
    @Autowired
    private PuitRepository puitRepository;
    
    @Autowired
    private PhaseRepository phaseRepository;

    @Transactional(readOnly = true)
    public List<DailyReport> findAll() {
        List<DailyReport> reports = dailyReportRepository.findAll();
        // Initialize collections to avoid lazy loading issues
        reports.forEach(this::initializeCollections);
        return reports;
    }

    @Transactional(readOnly = true)
    public Optional<DailyReport> findById(Long id) {
        Optional<DailyReport> report = dailyReportRepository.findById(id);
        report.ifPresent(this::initializeCollections);
        return report;
    }
    
    @Transactional(readOnly = true)
    public List<DailyReport> findByPuit(Long puitId) {
        List<DailyReport> reports = puitRepository.findById(puitId)
                .map(puit -> dailyReportRepository.findByConcernedWell(puit))
                .orElse(List.of());
        reports.forEach(this::initializeCollections);
        return reports;
    }
    
    @Transactional(readOnly = true)
    public List<DailyReport> findByReportDate(LocalDate reportDate) {
        List<DailyReport> reports = dailyReportRepository.findByReportDate(reportDate);
        reports.forEach(this::initializeCollections);
        return reports;
    }
    
    @Transactional(readOnly = true)
    public List<DailyReport> findByPuitAndDate(Long puitId, LocalDate reportDate) {
        List<DailyReport> reports = puitRepository.findById(puitId)
                .map(puit -> dailyReportRepository.findByConcernedWellAndReportDate(puit, reportDate))
                .orElse(List.of());
        reports.forEach(this::initializeCollections);
        return reports;
    }

    private void initializeCollections(DailyReport dailyReport) {
        // Force initialization of lazy collections
        if (dailyReport.getOperationsPerformed() != null) {
            dailyReport.getOperationsPerformed().size();
        }
        if (dailyReport.getIndicators() != null) {
            dailyReport.getIndicators().size();
        }
    }

    public DailyReport create(DailyReport dailyReport) {
        if (dailyReport.getConcernedWell() == null || dailyReport.getConcernedWell().getId() == null) {
            throw new IllegalArgumentException("Concerned well is required");
        }
        
        if (dailyReport.getReportName() == null || dailyReport.getReportName().trim().isEmpty()) {
            throw new IllegalArgumentException("Report name is required");
        }
        
        if (dailyReport.getReportDate() == null) {
            throw new IllegalArgumentException("Report date is required");
        }

        // Validate and set concerned well
        dailyReport.setConcernedWell(puitRepository.findById(dailyReport.getConcernedWell().getId())
                .orElseThrow(() -> new IllegalArgumentException("Concerned well not found")));

        // Validate and set current phase if provided
        if (dailyReport.getCurrentPhase() != null && dailyReport.getCurrentPhase().getId() != null) {
            dailyReport.setCurrentPhase(phaseRepository.findById(dailyReport.getCurrentPhase().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Current phase not found")));
        }

        // Set default values
        if (dailyReport.getCurrentDepth() == null) {
            dailyReport.setCurrentDepth(0.0);
        }
        
        if (dailyReport.getDailyCost() == null) {
            dailyReport.setDailyCost(0.0);
        }

        return dailyReportRepository.save(dailyReport);
    }

    public Optional<DailyReport> update(Long id, DailyReport dailyReportData) {
        return dailyReportRepository.findById(id)
                .map(dailyReport -> {
                    if (dailyReportData.getReportName() != null) {
                        dailyReport.setReportName(dailyReportData.getReportName());
                    }
                    
                    if (dailyReportData.getReportDate() != null) {
                        dailyReport.setReportDate(dailyReportData.getReportDate());
                    }
                    
                    if (dailyReportData.getConcernedWell() != null && dailyReportData.getConcernedWell().getId() != null) {
                        puitRepository.findById(dailyReportData.getConcernedWell().getId())
                                .ifPresent(dailyReport::setConcernedWell);
                    }
                    
                    if (dailyReportData.getCurrentPhase() != null && dailyReportData.getCurrentPhase().getId() != null) {
                        phaseRepository.findById(dailyReportData.getCurrentPhase().getId())
                                .ifPresent(dailyReport::setCurrentPhase);
                    }
                    
                    if (dailyReportData.getCurrentDepth() != null) {
                        dailyReport.setCurrentDepth(dailyReportData.getCurrentDepth());
                    }
                    
                    if (dailyReportData.getLithology() != null) {
                        dailyReport.setLithology(dailyReportData.getLithology());
                    }
                    
                    if (dailyReportData.getDailyCost() != null) {
                        dailyReport.setDailyCost(dailyReportData.getDailyCost());
                    }

                    return dailyReportRepository.save(dailyReport);
                });
    }

    public boolean delete(Long id) {
        return dailyReportRepository.findById(id)
            .map(dailyReport -> {
                dailyReportRepository.delete(dailyReport);
                return true;
            })
            .orElse(false);
    }
}

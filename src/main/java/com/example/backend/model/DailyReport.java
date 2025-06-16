package com.example.backend.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "daily_reports")
public class DailyReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "daily_reports_seq")
    @SequenceGenerator(name = "daily_reports_seq", sequenceName = "daily_reports_seq", allocationSize = 1)
    private Long id;
    
    @Column(name = "report_name", nullable = false)
    private String reportName;
    
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "puit_id", nullable = false)
    private Puit concernedWell;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_phase_id")
    private Phase currentPhase;
    
    @Column(name = "current_depth")
    private Double currentDepth;
    
    private String lithology;
    
    @Column(name = "daily_cost")
    private Double dailyCost;
    
    @OneToMany(mappedBy = "dailyReport", fetch = FetchType.LAZY)
    @JsonManagedReference("dailyReport-operations")
    private List<Operation> operationsPerformed;
    
    @OneToMany(mappedBy = "dailyReport", fetch = FetchType.LAZY)
    @JsonManagedReference("dailyReport-indicators")
    private List<Indicateur> indicators;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getReportName() {
        return reportName;
    }
    
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
    
    public LocalDate getReportDate() {
        return reportDate;
    }
    
    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }
    
    public Puit getConcernedWell() {
        return concernedWell;
    }
    
    public void setConcernedWell(Puit concernedWell) {
        this.concernedWell = concernedWell;
    }
    
    public Phase getCurrentPhase() {
        return currentPhase;
    }
    
    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }
    
    public Double getCurrentDepth() {
        return currentDepth;
    }
    
    public void setCurrentDepth(Double currentDepth) {
        this.currentDepth = currentDepth;
    }
    
    public String getLithology() {
        return lithology;
    }
    
    public void setLithology(String lithology) {
        this.lithology = lithology;
    }
    
    public Double getDailyCost() {
        return dailyCost;
    }
    
    public void setDailyCost(Double dailyCost) {
        this.dailyCost = dailyCost;
    }
    
    public List<Operation> getOperationsPerformed() {
        return operationsPerformed;
    }
    
    public void setOperationsPerformed(List<Operation> operationsPerformed) {
        this.operationsPerformed = operationsPerformed;
    }
    
    public List<Indicateur> getIndicators() {
        return indicators;
    }
    
    public void setIndicators(List<Indicateur> indicators) {
        this.indicators = indicators;
    }
}

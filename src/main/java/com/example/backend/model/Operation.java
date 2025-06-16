package com.example.backend.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "operations")
public class Operation {
    
    public enum Statut {
        PLANIFIE("Planifié"),
        EN_COURS("En cours"),
        TERMINE("Terminé"),
        PROBLEME("Problème"),
        ANNULE("Annulé");
        
        private final String label;
        
        Statut(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
        
        public static Statut fromLabel(String label) {
            for (Statut statut : Statut.values()) {
                if (statut.getLabel().equals(label)) {
                    return statut;
                }
            }
            throw new IllegalArgumentException("Unknown statut: " + label);
        }
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "phase_id")
    private Phase phase;
    
    @ManyToOne
    @JoinColumn(name = "type_operation_code")
    private TypeOperation typeOperation;
    
    private String description;
        
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut;
    
    @Column(name = "cout_prevu")
    private Double coutPrevu;

    @Column(name = "cout_reel")
    private Double coutReel;


    @ManyToOne
    @JoinColumn(name = "created_by")
    private Utilisateur createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "daily_report_id")
    @JsonBackReference("dailyReport-operations")
    private DailyReport dailyReport;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Phase getPhase() {
        return phase;
    }
    
    public void setPhase(Phase phase) {
        this.phase = phase;
    }
    
    public TypeOperation getTypeOperation() {
        return typeOperation;
    }
    
    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    

    
    public Statut getStatut() {
        return statut;
    }
    
    public void setStatut(Statut statut) {
        this.statut = statut;
    }
    
    public Utilisateur getCreatedBy() {
        return createdBy;
    }

    public Double getCoutPrev() {
        return coutPrevu;
    }

    public void setCoutPrev(Double coutPrev) {
        this.coutPrevu = coutPrev;
    }

    public Double getCoutReel() {
        return coutReel;
    }

    public void setCoutReel(Double coutReel) {
        this.coutReel = coutReel;
    }
    
    public void setCreatedBy(Utilisateur createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public DailyReport getDailyReport() {
        return dailyReport;
    }
    
    public void setDailyReport(DailyReport dailyReport) {
        this.dailyReport = dailyReport;
    }
}

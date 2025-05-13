package com.example.backend.model;

import java.time.LocalDate;

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
@Table(name = "phases")
public class Phase {
    
    public enum Diametre {
        POUCES_26("26\""),
        POUCES_16("16\""),
        POUCES_12_25("12 1/4\""),
        POUCES_8_5("8 1/2\"");
        
        private String label;
        
        Diametre(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "forage_id")
    private Forage forage;
    
    @Column(name = "numero_phase")
    private Integer numeroPhase;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Diametre diametre;
    
    private String description;
    
    @Column(name = "profondeur_prevue")
    private Double profondeurPrevue;
    
    @Column(name = "profondeur_reelle")
    private Double profondeurReelle;
    
    @Column(name = "date_debut_prevue")
    private LocalDate dateDebutPrevue;
    
    @Column(name = "date_debut_reelle")
    private LocalDate dateDebutReelle;
    
    @Column(name = "date_fin_prevue")
    private LocalDate dateFinPrevue;
    
    @Column(name = "date_fin_reelle")
    private LocalDate dateFinReelle;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Forage getForage() {
        return forage;
    }
    
    public void setForage(Forage forage) {
        this.forage = forage;
    }
    
    public Integer getNumeroPhase() {
        return numeroPhase;
    }
    
    public void setNumeroPhase(Integer numeroPhase) {
        this.numeroPhase = numeroPhase;
    }
    
    public Diametre getDiametre() {
        return diametre;
    }
    
    public void setDiametre(Diametre diametre) {
        this.diametre = diametre;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Double getProfondeurPrevue() {
        return profondeurPrevue;
    }
    
    public void setProfondeurPrevue(Double profondeurPrevue) {
        this.profondeurPrevue = profondeurPrevue;
    }
    
    public Double getProfondeurReelle() {
        return profondeurReelle;
    }
    
    public void setProfondeurReelle(Double profondeurReelle) {
        this.profondeurReelle = profondeurReelle;
    }
    
    public LocalDate getDateDebutPrevue() {
        return dateDebutPrevue;
    }
    
    public void setDateDebutPrevue(LocalDate dateDebutPrevue) {
        this.dateDebutPrevue = dateDebutPrevue;
    }
    
    public LocalDate getDateDebutReelle() {
        return dateDebutReelle;
    }
    
    public void setDateDebutReelle(LocalDate dateDebutReelle) {
        this.dateDebutReelle = dateDebutReelle;
    }
    
    public LocalDate getDateFinPrevue() {
        return dateFinPrevue;
    }
    
    public void setDateFinPrevue(LocalDate dateFinPrevue) {
        this.dateFinPrevue = dateFinPrevue;
    }
    
    public LocalDate getDateFinReelle() {
        return dateFinReelle;
    }
    
    public void setDateFinReelle(LocalDate dateFinReelle) {
        this.dateFinReelle = dateFinReelle;
    }
}

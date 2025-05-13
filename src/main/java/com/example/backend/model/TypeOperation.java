package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "type_operations")
public class TypeOperation {
    
    @Id
    private String code;
    
    private String nom;
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "phase_applicable_id")
    private Phase phaseApplicable;
    
    // Getters and Setters
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Phase getPhaseApplicable() {
        return phaseApplicable;
    }
    
    public void setPhaseApplicable(Phase phaseApplicable) {
        this.phaseApplicable = phaseApplicable;
    }
}

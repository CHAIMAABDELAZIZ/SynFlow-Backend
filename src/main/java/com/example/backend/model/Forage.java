package com.example.backend.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "forages")
public class Forage {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double cout;
    private LocalDate date_debut;
    private LocalDate date_fin;
    @ManyToOne
    @JoinColumn(name = "puit_id") // colonne dans la table "forages"
    private Puit puit;
  
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public Puit getPuit() {
        return puit;
    }

    public void setPuits(Puit puit) { 
        this.puit = puit;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Double getCout() {
        return cout;
    }
    
    public void setCout(Double cout) {
        this.cout = cout;
    }
    
    public LocalDate getDate_debut() {
        return date_debut;
    }
    
    public void setDate_debut(LocalDate date_debut) {
        this.date_debut = date_debut;
    }
    
    public LocalDate getDate_fin() {
        return date_fin;
    }
    
    public void setDate_fin(LocalDate date_fin) {
        this.date_fin = date_fin;
    }
}

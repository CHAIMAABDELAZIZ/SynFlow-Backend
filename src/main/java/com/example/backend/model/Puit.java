package com.example.backend.model;

import java.time.LocalDateTime;

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
@Table(name = "puits")
public class Puit {
    
    public enum Statut {
        EN_COURS("En cours"),
        TERMINE("Terminé"),
        ABANDONNE("Abandonné"),
        PLANIFIE("Planifié");
        
        private String label;
        
        Statut(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    private String type;
    
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
    
    private Double coord_x;
    private Double coord_y;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut;
    
    private String created_by;
    private LocalDateTime created_at;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Region getRegion() {
        return region;
    }
    
    public void setRegion(Region region) {
        this.region = region;
    }
    
    public Double getCoord_x() {
        return coord_x;
    }
    
    public void setCoord_x(Double coord_x) {
        this.coord_x = coord_x;
    }
    
    public Double getCoord_y() {
        return coord_y;
    }
    
    public void setCoord_y(Double coord_y) {
        this.coord_y = coord_y;
    }
    
    public Statut getStatut() {
        return statut;
    }
    
    public void setStatut(Statut statut) {
        this.statut = statut;
    }
    
    public String getCreated_by() {
        return created_by;
    }
    
    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
    
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}

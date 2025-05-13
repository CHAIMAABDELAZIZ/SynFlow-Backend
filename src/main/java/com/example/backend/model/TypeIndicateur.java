package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "type_indicateurs")
public class TypeIndicateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    private String unite;
    
    @ManyToOne
    @JoinColumn(name = "type_operation_code")
    private TypeOperation typeOperation;
    
    private Boolean estObligatoire;
    
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
    
    public String getUnite() {
        return unite;
    }
    
    public void setUnite(String unite) {
        this.unite = unite;
    }
    
    public TypeOperation getTypeOperation() {
        return typeOperation;
    }
    
    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }
    
    public Boolean getEstObligatoire() {
        return estObligatoire;
    }
    
    public void setEstObligatoire(Boolean estObligatoire) {
        this.estObligatoire = estObligatoire;
    }
}

package com.example.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents")
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "puit_id")
    private Puit puit;
    
    @ManyToOne
    @JoinColumn(name = "phase_id")
    private Phase phase;
    
    @ManyToOne
    @JoinColumn(name = "operation_id")
    private Operation operation;
    
    private String nom;
    private String type;
    private String chemin;
    private Long taille;
    
    @Column(name = "date_upload")
    private LocalDateTime dateUpload;
    
    @ManyToOne
    @JoinColumn(name = "uploade_par")
    private Utilisateur uploadePar;
    
    private String description;
    
    @Column(name = "est_public")
    private Boolean estPublic;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Puit getPuit() {
        return puit;
    }
    
    public void setPuit(Puit puit) {
        this.puit = puit;
    }
    
    public Phase getPhase() {
        return phase;
    }
    
    public void setPhase(Phase phase) {
        this.phase = phase;
    }
    
    public Operation getOperation() {
        return operation;
    }
    
    public void setOperation(Operation operation) {
        this.operation = operation;
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
    
    public String getChemin() {
        return chemin;
    }
    
    public void setChemin(String chemin) {
        this.chemin = chemin;
    }
    
    public Long getTaille() {
        return taille;
    }
    
    public void setTaille(Long taille) {
        this.taille = taille;
    }
    
    public LocalDateTime getDateUpload() {
        return dateUpload;
    }
    
    public void setDateUpload(LocalDateTime dateUpload) {
        this.dateUpload = dateUpload;
    }
    
    public Utilisateur getUploadePar() {
        return uploadePar;
    }
    
    public void setUploadePar(Utilisateur uploadePar) {
        this.uploadePar = uploadePar;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getEstPublic() {
        return estPublic;
    }
    
    public void setEstPublic(Boolean estPublic) {
        this.estPublic = estPublic;
    }
}

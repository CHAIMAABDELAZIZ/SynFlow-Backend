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
@Table(name = "indicateurs")
public class Indicateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "operation_id")
    private Operation operation;
    
    @ManyToOne
    @JoinColumn(name = "type_indicateur_id")
    private TypeIndicateur typeIndicateur;
    
    @Column(name = "valeur_prevue")
    private Double valeurPrevue;
    
    @Column(name = "valeur_reelle")
    private Double valeurReelle;
    
    @Column(name = "date_mesure")
    private LocalDateTime dateMesure;
    
    private String commentaire;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Operation getOperation() {
        return operation;
    }
    
    public void setOperation(Operation operation) {
        this.operation = operation;
    }
    
    public TypeIndicateur getTypeIndicateur() {
        return typeIndicateur;
    }
    
    public void setTypeIndicateur(TypeIndicateur typeIndicateur) {
        this.typeIndicateur = typeIndicateur;
    }
    
    public Double getValeurPrevue() {
        return valeurPrevue;
    }
    
    public void setValeurPrevue(Double valeurPrevue) {
        this.valeurPrevue = valeurPrevue;
    }
    
    public Double getValeurReelle() {
        return valeurReelle;
    }
    
    public void setValeurReelle(Double valeurReelle) {
        this.valeurReelle = valeurReelle;
    }
    
    public LocalDateTime getDateMesure() {
        return dateMesure;
    }
    
    public void setDateMesure(LocalDateTime dateMesure) {
        this.dateMesure = dateMesure;
    }
    
    public String getCommentaire() {
        return commentaire;
    }
    
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}

package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservoirs")
public class Reservoir {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "puit_id")
    private Puit puit;
    
    private String nom;
    private String natureFluide;
    private Double hauteurUtile;
    private String contactFluide;
    private Double netPay;
    private Double debit;
    private Double pressionTete;
    
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
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getNatureFluide() {
        return natureFluide;
    }
    
    public void setNatureFluide(String natureFluide) {
        this.natureFluide = natureFluide;
    }
    
    public Double getHauteurUtile() {
        return hauteurUtile;
    }
    
    public void setHauteurUtile(Double hauteurUtile) {
        this.hauteurUtile = hauteurUtile;
    }
    
    public String getContactFluide() {
        return contactFluide;
    }
    
    public void setContactFluide(String contactFluide) {
        this.contactFluide = contactFluide;
    }
    
    public Double getNetPay() {
        return netPay;
    }
    
    public void setNetPay(Double netPay) {
        this.netPay = netPay;
    }
    
    public Double getDebit() {
        return debit;
    }
    
    public void setDebit(Double debit) {
        this.debit = debit;
    }
    
    public Double getPressionTete() {
        return pressionTete;
    }
    
    public void setPressionTete(Double pressionTete) {
        this.pressionTete = pressionTete;
    }
}

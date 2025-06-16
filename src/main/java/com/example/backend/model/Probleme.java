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
@Table(name = "problemes")
public class Probleme {
    
    public enum Type {
        COUT("Dépassement des coûts"),
        DELAI("Dépassement des délais"),
        TECHNIQUE("Problème technique"),
        SECURITE("Problème sécurité");
        
        private final String label;
        
        Type(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
        
        public static Type fromLabel(String label) {
            for (Type type : Type.values()) {
                if (type.getLabel().equals(label)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown type: " + label);
        }
    }
    
    public enum Gravite {
        FAIBLE("Faible"),
        MODEREE("Modérée"),
        CRITIQUE("Critique");
        
        private final String label;
        
        Gravite(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
        
        public static Gravite fromLabel(String label) {
            for (Gravite gravite : Gravite.values()) {
                if (gravite.getLabel().equals(label)) {
                    return gravite;
                }
            }
            throw new IllegalArgumentException("Unknown gravite: " + label);
        }
    }
    
    public enum Statut {
        OUVERT("Ouvert"),
        EN_COURS("En cours"),
        RESOLU("Résolu"),
        FERME("Fermé");
        
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
    @JoinColumn(name = "operation_id")
    private Operation operation;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Type type;
    
    @ManyToOne
    @JoinColumn(name = "signale_par_id")
    private Utilisateur signalePar;
    
    @ManyToOne
    @JoinColumn(name = "resolu_par_id")
    private Utilisateur resoluPar;
    
    @Column(length = 1000)
    private String description;
    
    @Column(name = "date_detection")
    private LocalDate dateDetection;
    
    @Column(name = "date_resolution")
    private LocalDate dateResolution;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private Gravite gravite;
    
    @Column(name = "solution_propose", length = 1000)
    private String solutionPropose;
    
    @Column(name = "solution_implemente", length = 1000)
    private String solutionImplemente;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private Statut statut;
    
    @Column(name = "impact_delai")
    private Integer impactDelai; // in days
    
    @Column(name = "impact_cout")
    private Double impactCout; // in currency
    
    // Constructors
    public Probleme() {}
    
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
    
    public Type getType() {
        return type;
    }
    
    public void setType(Type type) {
        this.type = type;
    }
    
    public Utilisateur getSignalePar() {
        return signalePar;
    }
    
    public void setSignalePar(Utilisateur signalePar) {
        this.signalePar = signalePar;
    }
    
    public Utilisateur getResoluPar() {
        return resoluPar;
    }
    
    public void setResoluPar(Utilisateur resoluPar) {
        this.resoluPar = resoluPar;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getDateDetection() {
        return dateDetection;
    }
    
    public void setDateDetection(LocalDate dateDetection) {
        this.dateDetection = dateDetection;
    }
    
    public LocalDate getDateResolution() {
        return dateResolution;
    }
    
    public void setDateResolution(LocalDate dateResolution) {
        this.dateResolution = dateResolution;
    }
    
    public Gravite getGravite() {
        return gravite;
    }
    
    public void setGravite(Gravite gravite) {
        this.gravite = gravite;
    }
    
    public String getSolutionPropose() {
        return solutionPropose;
    }
    
    public void setSolutionPropose(String solutionPropose) {
        this.solutionPropose = solutionPropose;
    }
    
    public String getSolutionImplemente() {
        return solutionImplemente;
    }
    
    public void setSolutionImplemente(String solutionImplemente) {
        this.solutionImplemente = solutionImplemente;
    }
    
    public Statut getStatut() {
        return statut;
    }
    
    public void setStatut(Statut statut) {
        this.statut = statut;
    }
    
    public Integer getImpactDelai() {
        return impactDelai;
    }
    
    public void setImpactDelai(Integer impactDelai) {
        this.impactDelai = impactDelai;
    }
    
    public Double getImpactCout() {
        return impactCout;
    }
    
    public void setImpactCout(Double impactCout) {
        this.impactCout = impactCout;
    }
    
    @Override
    public String toString() {
        return "Probleme{" +
                "id=" + id +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", gravite=" + gravite +
                ", statut=" + statut +
                ", impactCout=" + impactCout +
                ", impactDelai=" + impactDelai +
                '}';
    }
}

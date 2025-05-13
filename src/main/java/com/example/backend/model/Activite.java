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
@Table(name = "activites")
public class Activite {
    
    public enum Type {
        CREATION("Création"),
        MODIFICATION("Modification"),
        SUPPRESSION("Suppression"),
        CONSULTATION("Consultation"),
        VALIDATION("Validation"),
        RESOLUTION_PROBLEME("Résolution de problème"),
        COMMENTAIRE("Commentaire"),
        MESURE("Prise de mesure"),
        CONNEXION("Connexion"),
        DECONNEXION("Déconnexion"),
        AUTRE("Autre");
        
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
    
    public enum TypeEntite {
        REGION("Region"),
        PUIT("Puit"),
        FORAGE("Forage"),
        PHASE("Phase"),
        OPERATION("Operation"),
        TYPE_OPERATION("Type d'opération"),
        TYPE_INDICATEUR("Type d'indicateur"),
        INDICATEUR("Indicateur"),
        PROBLEME("Problème"),
        UTILISATEUR("Utilisateur"),
        RESERVOIR("Reservoir"),
        AUTRE("Autre");
        
        private final String label;
        
        TypeEntite(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
        
        public static TypeEntite fromLabel(String label) {
            for (TypeEntite typeEntite : TypeEntite.values()) {
                if (typeEntite.getLabel().equals(label)) {
                    return typeEntite;
                }
            }
            throw new IllegalArgumentException("Unknown type entite: " + label);
        }
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Type type;
    
    private String description;
    
    private LocalDateTime date;
    
    @Column(name = "entite_concerne")
    private String entiteConcerne;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_entite", length = 30)
    private TypeEntite typeEntite;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    public Type getType() {
        return type;
    }
    
    public void setType(Type type) {
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public String getEntiteConcerne() {
        return entiteConcerne;
    }
    
    public void setEntiteConcerne(String entiteConcerne) {
        this.entiteConcerne = entiteConcerne;
    }
    
    public TypeEntite getTypeEntite() {
        return typeEntite;
    }
    
    public void setTypeEntite(TypeEntite typeEntite) {
        this.typeEntite = typeEntite;
    }
}

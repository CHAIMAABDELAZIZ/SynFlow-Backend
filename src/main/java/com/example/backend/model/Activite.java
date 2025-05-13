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
@Table(name = "activites")
public class Activite {
    
    public enum Type {
        CREATION, MODIFICATION, SUPPRESSION, CONSULTATION, VALIDATION, 
        RESOLUTION_PROBLEME, COMMENTAIRE, MESURE, CONNEXION, DECONNEXION, AUTRE;
        
        public String getLabel() {
            switch(this) {
                case CREATION: return "Création";
                case MODIFICATION: return "Modification";
                case SUPPRESSION: return "Suppression";
                case CONSULTATION: return "Consultation";
                case VALIDATION: return "Validation";
                case RESOLUTION_PROBLEME: return "Résolution de problème";
                case COMMENTAIRE: return "Commentaire";
                case MESURE: return "Prise de mesure";
                case CONNEXION: return "Connexion";
                case DECONNEXION: return "Déconnexion";
                case AUTRE: return "Autre";
                default: return name();
            }
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
        REGION, PUIT, FORAGE, PHASE, OPERATION, TYPE_OPERATION, 
        TYPE_INDICATEUR, INDICATEUR, PROBLEME, UTILISATEUR, RESERVOIR, AUTRE;
        
        public String getLabel() {
            switch (this) {
                case REGION: return "Region";
                case PUIT: return "Puit";
                case FORAGE: return "Forage";
                case PHASE: return "Phase";
                case OPERATION: return "Operation";
                case TYPE_OPERATION: return "Type d'opération";
                case TYPE_INDICATEUR: return "Type d'indicateur";
                case INDICATEUR: return "Indicateur";
                case PROBLEME: return "Problème";
                case UTILISATEUR: return "Utilisateur";
                case RESERVOIR: return "Reservoir";
                case AUTRE: return "Autre";
                default: return name();
            }
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
    
    @Column(length = 30)
    private String type;
    
    private String description;
    
    private LocalDateTime date;
    
    @Column(name = "entite_concerne")
    private String entiteConcerne;
    
    @Column(name = "type_entite", length = 30)
    private String typeEntite;
    
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
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
    
    public String getTypeEntite() {
        return typeEntite;
    }
    
    public void setTypeEntite(String typeEntite) {
        this.typeEntite = typeEntite;
    }
}

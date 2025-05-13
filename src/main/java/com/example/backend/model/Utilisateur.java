package com.example.backend.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {
    
    public enum Role {
        ADMIN("admin"),
        SUPERVISOR("supervisor"),
        ENGINEER("engineer"),
        VIEWER("viewer");
        
        private final String label;
        
        Role(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
        
        public static Role fromLabel(String label) {
            for (Role role : Role.values()) {
                if (role.getLabel().equals(label)) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Unknown role: " + label);
        }
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String nom;
    
    @NotBlank
    private String prenom;
    
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;
    
    @NotBlank
    @JsonIgnore
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    private LocalDateTime created_at;
    
    private LocalDateTime last_connection;
    
    private boolean status;  // true = connected, false = disconnected
    
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
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    
    public LocalDateTime getLast_connection() {
        return last_connection;
    }
    
    public void setLast_connection(LocalDateTime last_connection) {
        this.last_connection = last_connection;
    }
    
    public boolean isStatus() {
        return status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }
}

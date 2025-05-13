package com.example.backend.dto;

import com.example.backend.model.Utilisateur;

public class AuthResponse {
    private String token;
    private Utilisateur user;
    
    public AuthResponse(String token, Utilisateur user) {
        this.token = token;
        this.user = user;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public Utilisateur getUser() {
        return user;
    }
    
    public void setUser(Utilisateur user) {
        this.user = user;
    }
}

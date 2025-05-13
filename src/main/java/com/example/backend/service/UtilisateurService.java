package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.AuthRequest;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.model.Utilisateur;
import com.example.backend.model.Utilisateur.Role;
import com.example.backend.repository.UtilisateurRepository;
import com.example.backend.security.JwtTokenUtil;

@Service
@Transactional
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> findById(Long id) {
        return utilisateurRepository.findById(id);
    }
    
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        // Check if user already exists
        if (utilisateurRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(registerRequest.getNom());
        utilisateur.setPrenom(registerRequest.getPrenom());
        utilisateur.setEmail(registerRequest.getEmail());
        utilisateur.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        
        // Set role (default to VIEWER if not specified or invalid)
        try {
            if (registerRequest.getRole() != null) {
                utilisateur.setRole(Role.fromLabel(registerRequest.getRole()));
            } else {
                utilisateur.setRole(Role.VIEWER);
            }
        } catch (IllegalArgumentException e) {
            utilisateur.setRole(Role.VIEWER);
        }
        
        utilisateur.setCreated_at(LocalDateTime.now());
        utilisateur.setLast_connection(LocalDateTime.now());
        utilisateur.setStatus(true);
        
        utilisateur = utilisateurRepository.save(utilisateur);
        
        // Generate JWT token
        String token = jwtTokenUtil.generateToken(utilisateur);
        
        return new AuthResponse(token, utilisateur);
    }
    
    public AuthResponse login(AuthRequest authRequest) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findByEmail(authRequest.getEmail());
        
        if (optionalUtilisateur.isEmpty() || 
            !passwordEncoder.matches(authRequest.getPassword(), optionalUtilisateur.get().getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        
        Utilisateur utilisateur = optionalUtilisateur.get();
        utilisateur.setLast_connection(LocalDateTime.now());
        utilisateur.setStatus(true);
        utilisateur = utilisateurRepository.save(utilisateur);
        
        // Generate JWT token
        String token = jwtTokenUtil.generateToken(utilisateur);
        
        return new AuthResponse(token, utilisateur);
    }
    
    public boolean logout(Long id) {
        return utilisateurRepository.findById(id)
            .map(utilisateur -> {
                utilisateur.setStatus(false);
                utilisateurRepository.save(utilisateur);
                return true;
            })
            .orElse(false);
    }

    public Optional<Utilisateur> update(Long id, Utilisateur utilisateurData) {
        return utilisateurRepository.findById(id)
            .map(utilisateur -> {
                if (utilisateurData.getNom() != null) {
                    utilisateur.setNom(utilisateurData.getNom());
                }
                if (utilisateurData.getPrenom() != null) {
                    utilisateur.setPrenom(utilisateurData.getPrenom());
                }
                if (utilisateurData.getEmail() != null && 
                    !utilisateur.getEmail().equals(utilisateurData.getEmail()) &&
                    !utilisateurRepository.existsByEmail(utilisateurData.getEmail())) {
                    utilisateur.setEmail(utilisateurData.getEmail());
                }
                if (utilisateurData.getRole() != null) {
                    utilisateur.setRole(utilisateurData.getRole());
                }
                
                return utilisateurRepository.save(utilisateur);
            });
    }
    
    public Optional<Utilisateur> updatePassword(Long id, String currentPassword, String newPassword) {
        return utilisateurRepository.findById(id)
            .map(utilisateur -> {
                if (!passwordEncoder.matches(currentPassword, utilisateur.getPassword())) {
                    throw new IllegalArgumentException("Current password is incorrect");
                }
                utilisateur.setPassword(passwordEncoder.encode(newPassword));
                return utilisateurRepository.save(utilisateur);
            });
    }

    public boolean delete(Long id) {
        return utilisateurRepository.findById(id)
            .map(utilisateur -> {
                utilisateurRepository.delete(utilisateur);
                return true;
            })
            .orElse(false);
    }
}

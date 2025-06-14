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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class UtilisateurService {
    private static final Logger logger = LoggerFactory.getLogger(UtilisateurService.class);

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<Utilisateur> findAll() {
        logger.info("Fetching all users from MYAPP.UTILISATEURS");
        List<Utilisateur> users = utilisateurRepository.findAll();
        logger.info("Found {} users", users.size());
        return users;
    }

    public Optional<Utilisateur> findById(Long id) {
        logger.info("Fetching user with ID: {}", id);
        Optional<Utilisateur> user = utilisateurRepository.findById(id);
        logger.info("User found: {}", user.isPresent());
        return user;
    }

    public Optional<Utilisateur> findByEmail(String email) {
        logger.info("Fetching user with email: {}", email);
        Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);
        logger.info("User found: {}", user.isPresent());
        return user;
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        logger.info("Registering user with email: {}", registerRequest.getEmail());

        // Check if user already exists
        long startTime = System.currentTimeMillis();
        if (utilisateurRepository.existsByEmail(registerRequest.getEmail())) {
            logger.error("Email already in use: {}", registerRequest.getEmail());
            throw new IllegalArgumentException("Email already in use");
        }
        logger.info("Email check took {} ms", System.currentTimeMillis() - startTime);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(registerRequest.getNom());
        utilisateur.setPrenom(registerRequest.getPrenom());
        utilisateur.setEmail(registerRequest.getEmail());
        utilisateur.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Set role
        startTime = System.currentTimeMillis();
        try {
            if (registerRequest.getRole() != null) {
                utilisateur.setRole(Role.fromLabel(registerRequest.getRole()));
            } else {
                utilisateur.setRole(Role.VIEWER);
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid role: {}, defaulting to VIEWER", registerRequest.getRole());
            utilisateur.setRole(Role.VIEWER);
        }
        logger.info("Role assignment took {} ms", System.currentTimeMillis() - startTime);

        utilisateur.setCreated_at(LocalDateTime.now());
        utilisateur.setLast_connection(LocalDateTime.now());
        utilisateur.setStatus(true);

        // Save user
        startTime = System.currentTimeMillis();
        logger.info("Saving user to database");
        Utilisateur savedUser = utilisateurRepository.save(utilisateur);
        logger.info("User saved with ID: {}, took {} ms", savedUser.getId(), System.currentTimeMillis() - startTime);

        // Generate JWT token
        startTime = System.currentTimeMillis();
        String token = jwtTokenUtil.generateToken(savedUser);
        logger.info("JWT generation took {} ms", System.currentTimeMillis() - startTime);

        return new AuthResponse(token, savedUser);
    }

    public AuthResponse login(AuthRequest authRequest) {
        logger.info("Logging in user with email: {}", authRequest.getEmail());
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findByEmail(authRequest.getEmail());

        if (optionalUtilisateur.isEmpty() ||
                !passwordEncoder.matches(authRequest.getPassword(), optionalUtilisateur.get().getPassword())) {
            logger.error("Invalid email or password for: {}", authRequest.getEmail());
            throw new IllegalArgumentException("Invalid email or password");
        }

        Utilisateur utilisateur = optionalUtilisateur.get();
        utilisateur.setLast_connection(LocalDateTime.now());
        utilisateur.setStatus(true);
        utilisateur = utilisateurRepository.save(utilisateur);

        String token = jwtTokenUtil.generateToken(utilisateur);
        return new AuthResponse(token, utilisateur);
    }

    public boolean logout(Long id) {
        logger.info("Logging out user with ID: {}", id);
        return utilisateurRepository.findById(id)
                .map(utilisateur -> {
                    utilisateur.setStatus(false);
                    utilisateurRepository.save(utilisateur);
                    return true;
                })
                .orElse(false);
    }

    public Optional<Utilisateur> update(Long id, Utilisateur utilisateurData) {
        logger.info("Updating user with ID: {}", id);
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
        logger.info("Updating password for user with ID: {}", id);
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
        logger.info("Deleting user with ID: {}", id);
        return utilisateurRepository.findById(id)
                .map(utilisateur -> {
                    utilisateurRepository.delete(utilisateur);
                    return true;
                })
                .orElse(false);
    }
}
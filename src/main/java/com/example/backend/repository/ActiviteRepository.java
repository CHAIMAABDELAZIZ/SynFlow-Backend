package com.example.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Activite;
import com.example.backend.model.Utilisateur;

@Repository
public interface ActiviteRepository extends JpaRepository<Activite, Long> {
    List<Activite> findByUtilisateur(Utilisateur utilisateur);
    List<Activite> findByType(Activite.Type type);
    List<Activite> findByDateBetween(LocalDateTime debut, LocalDateTime fin);
    List<Activite> findByTypeEntite(Activite.TypeEntite typeEntite);
    List<Activite> findByEntiteConcerne(String entiteConcerne);
    
    // Paginated queries for large result sets
    Page<Activite> findAll(Pageable pageable);
    Page<Activite> findByUtilisateur(Utilisateur utilisateur, Pageable pageable);
    Page<Activite> findByType(Activite.Type type, Pageable pageable);
}

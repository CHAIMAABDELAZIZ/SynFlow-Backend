package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Operation;
import com.example.backend.model.Probleme;
import com.example.backend.model.Utilisateur;

@Repository
public interface ProblemeRepository extends JpaRepository<Probleme, Long> {
    List<Probleme> findByOperation(Operation operation);
    List<Probleme> findByType(Probleme.Type type);
    List<Probleme> findBySignalePar(Utilisateur signalePar);
    List<Probleme> findByResoluPar(Utilisateur resoluPar);
    List<Probleme> findByGravite(Probleme.Gravite gravite);
    List<Probleme> findByStatut(Probleme.Statut statut);
    
    // Find existing problems to avoid duplicates
    List<Probleme> findByOperationAndTypeAndStatutNot(Operation operation, Probleme.Type type, Probleme.Statut statut);
    List<Probleme> findByDescriptionContainingAndStatutNot(String description, Probleme.Statut statut);
}

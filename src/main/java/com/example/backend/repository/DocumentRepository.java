package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Document;
import com.example.backend.model.Operation;
import com.example.backend.model.Phase;
import com.example.backend.model.Puit;
import com.example.backend.model.Utilisateur;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByPuit(Puit puit);
    List<Document> findByPhase(Phase phase);
    List<Document> findByOperation(Operation operation);
    List<Document> findByUploadePar(Utilisateur utilisateur);
    List<Document> findByEstPublic(Boolean estPublic);
    List<Document> findByType(String type);
}

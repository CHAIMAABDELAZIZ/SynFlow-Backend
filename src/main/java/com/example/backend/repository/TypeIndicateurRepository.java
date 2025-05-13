package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.TypeIndicateur;
import com.example.backend.model.TypeOperation;

@Repository
public interface TypeIndicateurRepository extends JpaRepository<TypeIndicateur, Long> {
    List<TypeIndicateur> findByTypeOperation(TypeOperation typeOperation);
    List<TypeIndicateur> findByEstObligatoire(Boolean estObligatoire);
}

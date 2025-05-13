package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Indicateur;
import com.example.backend.model.Operation;
import com.example.backend.model.TypeIndicateur;

@Repository
public interface IndicateurRepository extends JpaRepository<Indicateur, Long> {
    List<Indicateur> findByOperation(Operation operation);
    List<Indicateur> findByTypeIndicateur(TypeIndicateur typeIndicateur);
    List<Indicateur> findByOperationAndTypeIndicateur(Operation operation, TypeIndicateur typeIndicateur);
}

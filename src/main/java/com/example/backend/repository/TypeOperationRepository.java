package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Phase;
import com.example.backend.model.TypeOperation;

@Repository
public interface TypeOperationRepository extends JpaRepository<TypeOperation, String> {
    List<TypeOperation> findByPhaseApplicable(Phase phase);
}

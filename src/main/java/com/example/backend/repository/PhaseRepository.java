package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Forage;
import com.example.backend.model.Phase;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long> {
    List<Phase> findByForage(Forage forage);
    List<Phase> findByForageOrderByNumeroPhase(Forage forage);
}

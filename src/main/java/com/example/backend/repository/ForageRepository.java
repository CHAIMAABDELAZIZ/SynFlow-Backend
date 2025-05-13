package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Forage;

@Repository
public interface ForageRepository extends JpaRepository<Forage, Long> {
}

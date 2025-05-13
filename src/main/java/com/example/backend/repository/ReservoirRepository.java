package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Puit;
import com.example.backend.model.Reservoir;

@Repository
public interface ReservoirRepository extends JpaRepository<Reservoir, Long> {
    List<Reservoir> findByPuit(Puit puit);
}

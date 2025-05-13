package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
}

package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Puit;
import com.example.backend.model.Region;
@Repository
public interface PuitRepository extends JpaRepository<Puit, Long> {
    List<Puit> findByRegion(Region region);
}

package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Region;
import com.example.backend.repository.RegionRepository;

@Service
@Transactional
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<Region> findAll() {
        return regionRepository.findAll();
    }

    public Optional<Region> findById(Long id) {
        return regionRepository.findById(id);
    }

    public Region create(Region region) {
        // Set creation time if not provided
        if (region.getCreated_at() == null) {
            region.setCreated_at(LocalDateTime.now());
        }
        return regionRepository.save(region);
    }

    public Optional<Region> update(Long id, Region regionData) {
        return regionRepository.findById(id)
            .map(region -> {
                region.setNom(regionData.getNom());
                region.setCode(regionData.getCode());
                region.setLocalisation(regionData.getLocalisation());
                region.setResponsable(regionData.getResponsable());
                // Don't update created_at
                return regionRepository.save(region);
            });
    }

    public boolean delete(Long id) {
        return regionRepository.findById(id)
            .map(region -> {
                regionRepository.delete(region);
                return true;
            })
            .orElse(false);
    }
}

package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Puit;
import com.example.backend.repository.PuitRepository;
import com.example.backend.repository.RegionRepository;

@Service
@Transactional
public class PuitService {

    @Autowired
    private PuitRepository puitRepository;
    
    @Autowired
    private RegionRepository regionRepository;

    public List<Puit> findAll() {
        return puitRepository.findAll();
    }

    public Optional<Puit> findById(Long id) {
        return puitRepository.findById(id);
    }
    
    public List<Puit> findByRegion(Long regionId) {
        return regionRepository.findById(regionId)
                .map(region -> puitRepository.findByRegion(region))
                .orElse(List.of());
    }

    public Puit create(Puit puit) { 
        // Set creation time if not provided
        if (puit.getCreated_at() == null) {
            puit.setCreated_at(LocalDateTime.now());
        }
        return puitRepository.save(puit);
    }

    public Optional<Puit> update(Long id, Puit puitData) {
        return puitRepository.findById(id)
            .map(puit -> {
                puit.setNom(puitData.getNom());
                puit.setType(puitData.getType());
                
                // Handle Region relationship
                if (puitData.getRegion() != null && puitData.getRegion().getId() != null) {
                    regionRepository.findById(puitData.getRegion().getId())
                        .ifPresent(puit::setRegion);
                }
                
                puit.setCoord_x(puitData.getCoord_x());
                puit.setCoord_y(puitData.getCoord_y());
                puit.setStatut(puitData.getStatut());
                // Don't update created_by and created_at
                return puitRepository.save(puit);
            });
    }

    public boolean delete(Long id) {
        return puitRepository.findById(id)
            .map(puit -> {
                puitRepository.delete(puit);
                return true;
            })
            .orElse(false);
    }
}

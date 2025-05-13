package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Phase;
import com.example.backend.repository.ForageRepository;
import com.example.backend.repository.PhaseRepository;

@Service
@Transactional
public class PhaseService {

    @Autowired
    private PhaseRepository phaseRepository;
    
    @Autowired
    private ForageRepository forageRepository;

    public List<Phase> findAll() {
        return phaseRepository.findAll();
    }

    public Optional<Phase> findById(Long id) {
        return phaseRepository.findById(id);
    }
    
    public List<Phase> findByForage(Long forageId) {
        return forageRepository.findById(forageId)
                .map(forage -> phaseRepository.findByForageOrderByNumeroPhase(forage))
                .orElse(List.of());
    }

    public Phase create(Phase phase) {
        // Validate phase number is between 1 and 4
        if (phase.getNumeroPhase() < 1 || phase.getNumeroPhase() > 4) {
            throw new IllegalArgumentException("Numéro de phase doit être entre 1 et 4");
        }
        return phaseRepository.save(phase);
    }

    public Optional<Phase> update(Long id, Phase phaseData) {
        // Validate phase number is between 1 and 4
        if (phaseData.getNumeroPhase() != null && 
            (phaseData.getNumeroPhase() < 1 || phaseData.getNumeroPhase() > 4)) {
            throw new IllegalArgumentException("Numéro de phase doit être entre 1 et 4");
        }
        
        return phaseRepository.findById(id)
            .map(phase -> {
                // Handle Forage relationship
                if (phaseData.getForage() != null && phaseData.getForage().getId() != null) {
                    forageRepository.findById(phaseData.getForage().getId())
                        .ifPresent(phase::setForage);
                }
                
                if (phaseData.getNumeroPhase() != null) {
                    phase.setNumeroPhase(phaseData.getNumeroPhase());
                }
                if (phaseData.getDiametre() != null) {
                    phase.setDiametre(phaseData.getDiametre());
                }
                if (phaseData.getDescription() != null) {
                    phase.setDescription(phaseData.getDescription());
                }
                if (phaseData.getProfondeurPrevue() != null) {
                    phase.setProfondeurPrevue(phaseData.getProfondeurPrevue());
                }
                if (phaseData.getProfondeurReelle() != null) {
                    phase.setProfondeurReelle(phaseData.getProfondeurReelle());
                }
                if (phaseData.getDateDebutPrevue() != null) {
                    phase.setDateDebutPrevue(phaseData.getDateDebutPrevue());
                }
                if (phaseData.getDateDebutReelle() != null) {
                    phase.setDateDebutReelle(phaseData.getDateDebutReelle());
                }
                if (phaseData.getDateFinPrevue() != null) {
                    phase.setDateFinPrevue(phaseData.getDateFinPrevue());
                }
                if (phaseData.getDateFinReelle() != null) {
                    phase.setDateFinReelle(phaseData.getDateFinReelle());
                }
                
                return phaseRepository.save(phase);
            });
    }

    public boolean delete(Long id) {
        return phaseRepository.findById(id)
            .map(phase -> {
                phaseRepository.delete(phase);
                return true;
            })
            .orElse(false);
    }
}

package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Forage;
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
                .map(forage -> phaseRepository.findByForage(forage))
                .orElse(List.of());
    }

    public Phase create(Phase phase) {
        try {
            System.out.println("=== PHASE SERVICE CREATE ===");
            System.out.println("Input phase: " + phase);
            
            // Validate required fields
            if (phase.getForage() == null || phase.getForage().getId() == null) {
                System.err.println("ERROR: Forage is null or has null ID");
                throw new IllegalArgumentException("Forage non spécifié ou ID manquant");
            }

            if (phase.getNumeroPhase() == null) {
                System.err.println("ERROR: Phase number is null");
                throw new IllegalArgumentException("Numéro de phase requis");
            }

            if (phase.getDiametre() == null) {
                System.err.println("ERROR: Phase diametre is null");
                throw new IllegalArgumentException("Diamètre de phase requis");
            }

            System.out.println("Validating forage ID: " + phase.getForage().getId());
            
            // Validate and set forage
            Forage forage = forageRepository.findById(phase.getForage().getId())
                    .orElseThrow(() -> {
                        System.err.println("ERROR: Forage not found with ID: " + phase.getForage().getId());
                        return new IllegalArgumentException("Forage introuvable avec l'ID: " + phase.getForage().getId());
                    });
            
            System.out.println("Found forage: " + forage.getId());
            phase.setForage(forage);

            // Check for duplicate phase number in the same forage
            List<Phase> existingPhases = phaseRepository.findByForageAndNumeroPhase(forage, phase.getNumeroPhase());
            if (!existingPhases.isEmpty()) {
                System.err.println("ERROR: Phase number " + phase.getNumeroPhase() + " already exists for forage " + forage.getId());
                throw new IllegalArgumentException("Une phase avec ce numéro existe déjà pour ce forage");
            }

            System.out.println("Saving phase to database...");
            Phase savedPhase = phaseRepository.save(phase);
            System.out.println("Phase saved successfully with ID: " + savedPhase.getId());
            System.out.println("=== PHASE SERVICE CREATE SUCCESS ===");
            
            return savedPhase;
        } catch (Exception e) {
            System.err.println("=== PHASE SERVICE CREATE ERROR ===");
            System.err.println("Error in PhaseService.create(): " + e.getMessage());
            System.err.println("Error class: " + e.getClass().getName());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Phase> update(Long id, Phase phaseData) {
        try {
            System.out.println("=== PHASE SERVICE UPDATE ===");
            System.out.println("Updating phase ID: " + id);
            
            return phaseRepository.findById(id)
                    .map(phase -> {
                        System.out.println("Found existing phase: " + phase.getId());
                        
                        // Handle Forage relationship
                        if (phaseData.getForage() != null && phaseData.getForage().getId() != null) {
                            forageRepository.findById(phaseData.getForage().getId())
                                    .ifPresent(forage -> {
                                        System.out.println("Setting forage: " + forage.getId());
                                        phase.setForage(forage);
                                    });
                        }

                        if (phaseData.getNumeroPhase() != null) {
                            System.out.println("Setting phase number: " + phaseData.getNumeroPhase());
                            phase.setNumeroPhase(phaseData.getNumeroPhase());
                        }

                        if (phaseData.getDiametre() != null) {
                            System.out.println("Setting diametre: " + phaseData.getDiametre());
                            phase.setDiametre(phaseData.getDiametre());
                        }

                        if (phaseData.getDescription() != null) {
                            System.out.println("Setting description: " + phaseData.getDescription());
                            phase.setDescription(phaseData.getDescription());
                        }

                        if (phaseData.getProfondeurPrevue() != null) {
                            phase.setProfondeurPrevue(phaseData.getProfondeurPrevue());
                        }

                        if (phaseData.getDateDebutPrevue() != null) {
                            System.out.println("Setting start date: " + phaseData.getDateDebutPrevue());
                            phase.setDateDebutPrevue(phaseData.getDateDebutPrevue());
                        }

                        if (phaseData.getDateFinPrevue() != null) {
                            System.out.println("Setting end date: " + phaseData.getDateFinPrevue());
                            phase.setDateFinPrevue(phaseData.getDateFinPrevue());
                        }

                        System.out.println("Saving updated phase...");
                        Phase saved = phaseRepository.save(phase);
                        System.out.println("Phase updated successfully");
                        return saved;
                    });
        } catch (Exception e) {
            System.err.println("=== PHASE SERVICE UPDATE ERROR ===");
            System.err.println("Error updating phase: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public boolean delete(Long id) {
        try {
            return phaseRepository.findById(id)
                .map(phase -> {
                    System.out.println("Deleting phase: " + id);
                    phaseRepository.delete(phase);
                    System.out.println("Phase deleted successfully");
                    return true;
                })
                .orElse(false);
        } catch (Exception e) {
            System.err.println("Error deleting phase: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

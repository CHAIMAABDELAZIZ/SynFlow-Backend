package com.example.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.DailyReport;
import com.example.backend.model.Operation;
import com.example.backend.model.Phase;
import com.example.backend.model.Probleme;
import com.example.backend.repository.ProblemeRepository;
import com.example.backend.repository.UtilisateurRepository;

@Service
@Transactional
public class ProblemeDetectionService {

    @Autowired
    private ProblemeRepository problemeRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Detect and create problems after a daily report is saved
     */
    public List<Probleme> detectAndCreateProblems(DailyReport dailyReport, List<Operation> updatedOperations) {
        List<Probleme> createdProblems = new ArrayList<>();
        
        System.out.println("=== PROBLEM DETECTION SERVICE ===");
        System.out.println("Checking " + updatedOperations.size() + " operations for problems");
        
        // Get system user for automatic problem detection (or use the report creator)
        var systemUser = utilisateurRepository.findById(1L).orElse(null);
        if (systemUser == null) {
            System.err.println("System user not found, creating problems without user reference");
        } else {
            System.out.println("Using system user: " + systemUser.getNom());
        }
        
        // 1. Check for operation cost overruns
        System.out.println("=== CHECKING COST OVERRUNS ===");
        for (Operation operation : updatedOperations) {
            System.out.println("Checking operation " + operation.getId() + ":");
            System.out.println("  - Description: " + operation.getDescription());
            System.out.println("  - CoutPrev: " + operation.getCoutPrev());
            System.out.println("  - CoutReel: " + operation.getCoutReel());
            
            if (operation.getCoutReel() != null && operation.getCoutPrev() != null) {
                if (operation.getCoutReel() > operation.getCoutPrev()) {
                    System.out.println("  *** COST OVERRUN DETECTED ***");
                    System.out.println("  Real cost (" + operation.getCoutReel() + ") > Planned cost (" + operation.getCoutPrev() + ")");
                    
                    Probleme costProblem = createCostOverrunProblem(operation, dailyReport, systemUser);
                    if (costProblem != null) {
                        createdProblems.add(costProblem);
                        System.out.println("  Created cost overrun problem with ID: " + costProblem.getId());
                    } else {
                        System.err.println("  Failed to create cost overrun problem");
                    }
                } else {
                    System.out.println("  No cost overrun (real <= planned)");
                }
            } else {
                System.out.println("  Skipping cost check - missing cost data");
                System.out.println("  CoutReel is null: " + (operation.getCoutReel() == null));
                System.out.println("  CoutPrev is null: " + (operation.getCoutPrev() == null));
            }
        }
        
        // 2. Check for phase-related problems
        System.out.println("=== CHECKING PHASE PROBLEMS ===");
        Phase currentPhase = dailyReport.getCurrentPhase();
        if (currentPhase != null) {
            System.out.println("Checking phase " + currentPhase.getId() + " (Phase " + currentPhase.getNumeroPhase() + ")");
            
            // Check depth overrun
            System.out.println("Checking depth overrun:");
            System.out.println("  - ProfondeurPrevue: " + currentPhase.getProfondeurPrevue());
            System.out.println("  - ProfondeurReelle: " + currentPhase.getProfondeurReelle());
            
            if (currentPhase.getProfondeurReelle() != null && currentPhase.getProfondeurPrevue() != null) {
                if (currentPhase.getProfondeurReelle() > currentPhase.getProfondeurPrevue()) {
                    System.out.println("  *** DEPTH OVERRUN DETECTED ***");
                    Probleme depthProblem = createDepthOverrunProblem(currentPhase, dailyReport, systemUser);
                    if (depthProblem != null) {
                        createdProblems.add(depthProblem);
                        System.out.println("  Created depth overrun problem with ID: " + depthProblem.getId());
                    }
                } else {
                    System.out.println("  No depth overrun");
                }
            } else {
                System.out.println("  Skipping depth check - missing depth data");
            }
            
            // Check schedule delays
            System.out.println("Checking schedule delays:");
            List<Probleme> scheduleProblems = checkScheduleDelays(currentPhase, dailyReport, systemUser);
            createdProblems.addAll(scheduleProblems);
            System.out.println("  Found " + scheduleProblems.size() + " schedule problems");
        } else {
            System.out.println("No current phase to check for phase problems");
        }
        
        System.out.println("=== DETECTION COMPLETE ===");
        System.out.println("Total problems created: " + createdProblems.size());
        
        return createdProblems;
    }

    private Probleme createCostOverrunProblem(Operation operation, DailyReport dailyReport, com.example.backend.model.Utilisateur systemUser) {
        System.out.println("=== CREATING COST OVERRUN PROBLEM ===");
        
        try {
            // Check if a similar problem already exists for this operation
            List<Probleme> existingProblems = problemeRepository.findByOperationAndTypeAndStatutNot(
                operation, Probleme.Type.COUT, Probleme.Statut.FERME);
            
            System.out.println("Found " + existingProblems.size() + " existing cost problems for this operation");
            
            if (!existingProblems.isEmpty()) {
                // Update existing problem with new cost information
                Probleme existingProblem = existingProblems.get(0);
                double overrun = operation.getCoutReel() - operation.getCoutPrev();
                existingProblem.setImpactCout(overrun);
                existingProblem.setDescription(String.format(
                    "Dépassement de coût détecté pour l'opération '%s'. Coût prévu: %.2f DZD, Coût réel: %.2f DZD, Dépassement: %.2f DZD",
                    operation.getDescription(),
                    operation.getCoutPrev(),
                    operation.getCoutReel(),
                    overrun
                ));
                System.out.println("Updated existing problem: " + existingProblem.getId());
                return problemeRepository.save(existingProblem);
            }
            
            // Create new cost overrun problem
            double overrun = operation.getCoutReel() - operation.getCoutPrev();
            System.out.println("Creating new cost overrun problem:");
            System.out.println("  - Overrun amount: " + overrun);
            
            Probleme probleme = new Probleme();
            probleme.setOperation(operation);
            probleme.setType(Probleme.Type.COUT);
            probleme.setDescription(String.format(
                "Dépassement de coût détecté pour l'opération '%s'. Coût prévu: %.2f DZD, Coût réel: %.2f DZD, Dépassement: %.2f DZD",
                operation.getDescription(),
                operation.getCoutPrev(),
                operation.getCoutReel(),
                overrun
            ));
            probleme.setDateDetection(LocalDate.now());
            probleme.setSignalePar(systemUser);
            probleme.setImpactCout(overrun);
            
            // Determine severity based on overrun percentage
            double overrunPercentage = (overrun / operation.getCoutPrev()) * 100;
            System.out.println("  - Overrun percentage: " + overrunPercentage + "%");
            
            if (overrunPercentage > 50) {
                probleme.setGravite(Probleme.Gravite.CRITIQUE);
            } else if (overrunPercentage > 20) {
                probleme.setGravite(Probleme.Gravite.MODEREE);
            } else {
                probleme.setGravite(Probleme.Gravite.FAIBLE);
            }
            
            probleme.setStatut(Probleme.Statut.OUVERT);
            probleme.setSolutionPropose("Analyser les causes du dépassement et ajuster les estimations futures.");
            
            System.out.println("  - Severity: " + probleme.getGravite());
            System.out.println("  - Status: " + probleme.getStatut());
            
            Probleme saved = problemeRepository.save(probleme);
            System.out.println("Successfully saved cost overrun problem with ID: " + saved.getId());
            return saved;
        } catch (Exception e) {
            System.err.println("Error saving cost overrun problem: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Probleme createDepthOverrunProblem(Phase phase, DailyReport dailyReport, com.example.backend.model.Utilisateur systemUser) {
        try {
            // Check if a similar problem already exists for this phase
            String depthDescription = String.format("Phase %d - Dépassement de profondeur", phase.getNumeroPhase());
            List<Probleme> existingProblems = problemeRepository.findByDescriptionContainingAndStatutNot(
                depthDescription, Probleme.Statut.FERME);
            
            if (!existingProblems.isEmpty()) {
                // Update existing problem
                Probleme existingProblem = existingProblems.get(0);
                double depthOverrun = phase.getProfondeurReelle() - phase.getProfondeurPrevue();
                existingProblem.setDescription(String.format(
                    "Phase %d - Dépassement de profondeur détecté. Profondeur prévue: %.2f m, Profondeur réelle: %.2f m, Dépassement: %.2f m",
                    phase.getNumeroPhase(),
                    phase.getProfondeurPrevue(),
                    phase.getProfondeurReelle(),
                    depthOverrun
                ));
                return problemeRepository.save(existingProblem);
            }
            
            // Create new depth overrun problem
            double depthOverrun = phase.getProfondeurReelle() - phase.getProfondeurPrevue();
            
            Probleme probleme = new Probleme();
            probleme.setType(Probleme.Type.TECHNIQUE);
            probleme.setDescription(String.format(
                "Phase %d - Dépassement de profondeur détecté. Profondeur prévue: %.2f m, Profondeur réelle: %.2f m, Dépassement: %.2f m",
                phase.getNumeroPhase(),
                phase.getProfondeurPrevue(),
                phase.getProfondeurReelle(),
                depthOverrun
            ));
            probleme.setDateDetection(LocalDate.now());
            probleme.setSignalePar(systemUser);
            
            // Determine severity based on depth overrun percentage
            double overrunPercentage = (depthOverrun / phase.getProfondeurPrevue()) * 100;
            if (overrunPercentage > 30) {
                probleme.setGravite(Probleme.Gravite.CRITIQUE);
            } else if (overrunPercentage > 10) {
                probleme.setGravite(Probleme.Gravite.MODEREE);
            } else {
                probleme.setGravite(Probleme.Gravite.FAIBLE);
            }
            
            probleme.setStatut(Probleme.Statut.OUVERT);
            probleme.setSolutionPropose("Réviser les paramètres de forage et les prévisions géologiques.");
            
            return problemeRepository.save(probleme);
        } catch (Exception e) {
            System.err.println("Error creating depth overrun problem: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private List<Probleme> checkScheduleDelays(Phase phase, DailyReport dailyReport, com.example.backend.model.Utilisateur systemUser) {
        List<Probleme> scheduleProblems = new ArrayList<>();
        LocalDate reportDate = dailyReport.getReportDate();
        
        // Check if start date is delayed
        if (phase.getDateDebutPrevue() != null && phase.getDateDebutReelle() != null) {
            if (phase.getDateDebutReelle().isAfter(phase.getDateDebutPrevue())) {
                Probleme startDelayProblem = createScheduleDelayProblem(
                    phase, "début", phase.getDateDebutPrevue(), phase.getDateDebutReelle(), 
                    dailyReport, systemUser);
                if (startDelayProblem != null) {
                    scheduleProblems.add(startDelayProblem);
                }
            }
        }
        
        // Check if current phase is behind schedule (if end date is planned but we're past it)
        if (phase.getDateFinPrevue() != null && phase.getDateFinReelle() == null) {
            if (reportDate.isAfter(phase.getDateFinPrevue())) {
                Probleme endDelayProblem = createScheduleDelayProblem(
                    phase, "fin", phase.getDateFinPrevue(), reportDate, 
                    dailyReport, systemUser);
                if (endDelayProblem != null) {
                    scheduleProblems.add(endDelayProblem);
                }
            }
        }
        
        // Check if end date is delayed (if phase is completed but late)
        if (phase.getDateFinPrevue() != null && phase.getDateFinReelle() != null) {
            if (phase.getDateFinReelle().isAfter(phase.getDateFinPrevue())) {
                Probleme endDelayProblem = createScheduleDelayProblem(
                    phase, "fin", phase.getDateFinPrevue(), phase.getDateFinReelle(), 
                    dailyReport, systemUser);
                if (endDelayProblem != null) {
                    scheduleProblems.add(endDelayProblem);
                }
            }
        }
        
        return scheduleProblems;
    }

    private Probleme createScheduleDelayProblem(Phase phase, String delayType, LocalDate plannedDate, 
                                               LocalDate actualDate, DailyReport dailyReport, com.example.backend.model.Utilisateur systemUser) {
        try {
            // Check if a similar problem already exists
            String delayDescription = String.format("Phase %d - Retard de %s", phase.getNumeroPhase(), delayType);
            List<Probleme> existingProblems = problemeRepository.findByDescriptionContainingAndStatutNot(
                delayDescription, Probleme.Statut.FERME);
            
            if (!existingProblems.isEmpty()) {
                // Update existing problem
                Probleme existingProblem = existingProblems.get(0);
                long delayDays = java.time.temporal.ChronoUnit.DAYS.between(plannedDate, actualDate);
                existingProblem.setImpactDelai((int) delayDays);
                existingProblem.setDescription(String.format(
                    "Phase %d - Retard de %s détecté. Date prévue: %s, Date réelle: %s, Retard: %d jours",
                    phase.getNumeroPhase(),
                    delayType,
                    plannedDate.toString(),
                    actualDate.toString(),
                    delayDays
                ));
                return problemeRepository.save(existingProblem);
            }
            
            // Create new schedule delay problem
            long delayDays = java.time.temporal.ChronoUnit.DAYS.between(plannedDate, actualDate);
            
            Probleme probleme = new Probleme();
            probleme.setType(Probleme.Type.DELAI);
            probleme.setDescription(String.format(
                "Phase %d - Retard de %s détecté. Date prévue: %s, Date réelle: %s, Retard: %d jours",
                phase.getNumeroPhase(),
                delayType,
                plannedDate.toString(),
                actualDate.toString(),
                delayDays
            ));
            probleme.setDateDetection(LocalDate.now());
            probleme.setSignalePar(systemUser);
            probleme.setImpactDelai((int) delayDays);
            
            // Determine severity based on delay duration
            if (delayDays > 14) {
                probleme.setGravite(Probleme.Gravite.CRITIQUE);
            } else if (delayDays > 7) {
                probleme.setGravite(Probleme.Gravite.MODEREE);
            } else {
                probleme.setGravite(Probleme.Gravite.FAIBLE);
            }
            
            probleme.setStatut(Probleme.Statut.OUVERT);
            probleme.setSolutionPropose("Analyser les causes du retard et ajuster la planification.");
            
            return problemeRepository.save(probleme);
        } catch (Exception e) {
            System.err.println("Error creating schedule delay problem: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

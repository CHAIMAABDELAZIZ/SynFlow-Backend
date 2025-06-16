package com.example.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.DailyReport;
import com.example.backend.model.Operation;
import com.example.backend.model.Probleme;
import com.example.backend.repository.OperationRepository;
import com.example.backend.repository.ProblemeRepository;
import com.example.backend.repository.UtilisateurRepository;

@Service
@Transactional
public class ProblemeService {

    @Autowired
    private ProblemeRepository problemeRepository;
    
    @Autowired
    private OperationRepository operationRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private ProblemeDetectionService problemeDetectionService;
    
    @Autowired
    private DailyReportService dailyReportService;

    public List<Probleme> findAll() {
        return problemeRepository.findAll();
    }

    public Optional<Probleme> findById(Long id) {
        return problemeRepository.findById(id);
    }
    
    public List<Probleme> findByOperation(Long operationId) {
        return operationRepository.findById(operationId)
                .map(operation -> problemeRepository.findByOperation(operation))
                .orElse(List.of());
    }
    
    public List<Probleme> findByType(String typeLabel) {
        try {
            Probleme.Type type = Probleme.Type.fromLabel(typeLabel);
            return problemeRepository.findByType(type);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }
    
    public List<Probleme> findBySignalePar(Long utilisateurId) {
        return utilisateurRepository.findById(utilisateurId)
                .map(utilisateur -> problemeRepository.findBySignalePar(utilisateur))
                .orElse(List.of());
    }
    
    public List<Probleme> findByResoluPar(Long utilisateurId) {
        return utilisateurRepository.findById(utilisateurId)
                .map(utilisateur -> problemeRepository.findByResoluPar(utilisateur))
                .orElse(List.of());
    }
    
    public List<Probleme> findByGravite(String graviteLabel) {
        try {
            Probleme.Gravite gravite = Probleme.Gravite.fromLabel(graviteLabel);
            return problemeRepository.findByGravite(gravite);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }
    
    public List<Probleme> findByStatut(String statutLabel) {
        try {
            Probleme.Statut statut = Probleme.Statut.fromLabel(statutLabel);
            return problemeRepository.findByStatut(statut);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    public Probleme create(Probleme probleme) {
        try {
            if (probleme.getDateDetection() == null) {
                probleme.setDateDetection(LocalDate.now());
            }
            
            // Default to OUVERT status if not specified
            if (probleme.getStatut() == null) {
                probleme.setStatut(Probleme.Statut.OUVERT);
            }
            
            System.out.println("Creating problem: " + probleme.getDescription());
            System.out.println("Problem type: " + probleme.getType());
            System.out.println("Problem gravity: " + probleme.getGravite());
            System.out.println("Problem status: " + probleme.getStatut());
            
            Probleme saved = problemeRepository.save(probleme);
            System.out.println("Successfully created problem with ID: " + saved.getId());
            return saved;
        } catch (Exception e) {
            System.err.println("Error creating problem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Probleme> update(Long id, Probleme problemeData) {
        return problemeRepository.findById(id)
            .map(probleme -> {
                // Handle Operation relationship
                if (problemeData.getOperation() != null && problemeData.getOperation().getId() != null) {
                    operationRepository.findById(problemeData.getOperation().getId())
                        .ifPresent(probleme::setOperation);
                }
                
                if (problemeData.getType() != null) {
                    probleme.setType(problemeData.getType());
                }
                
                // Handle SignalePar relationship
                if (problemeData.getSignalePar() != null && problemeData.getSignalePar().getId() != null) {
                    utilisateurRepository.findById(problemeData.getSignalePar().getId())
                        .ifPresent(probleme::setSignalePar);
                }
                
                // Handle ResoluPar relationship
                if (problemeData.getResoluPar() != null && problemeData.getResoluPar().getId() != null) {
                    utilisateurRepository.findById(problemeData.getResoluPar().getId())
                        .ifPresent(probleme::setResoluPar);
                }
                
                if (problemeData.getDescription() != null) {
                    probleme.setDescription(problemeData.getDescription());
                }
                if (problemeData.getDateDetection() != null) {
                    probleme.setDateDetection(problemeData.getDateDetection());
                }
                if (problemeData.getDateResolution() != null) {
                    probleme.setDateResolution(problemeData.getDateResolution());
                }
                if (problemeData.getGravite() != null) {
                    probleme.setGravite(problemeData.getGravite());
                }
                if (problemeData.getSolutionPropose() != null) {
                    probleme.setSolutionPropose(problemeData.getSolutionPropose());
                }
                if (problemeData.getSolutionImplemente() != null) {
                    probleme.setSolutionImplemente(problemeData.getSolutionImplemente());
                }
                if (problemeData.getStatut() != null) {
                    probleme.setStatut(problemeData.getStatut());
                    
                    // Automatically set resolution date when status changes to RESOLU
                    if (problemeData.getStatut() == Probleme.Statut.RESOLU && probleme.getDateResolution() == null) {
                        probleme.setDateResolution(LocalDate.now());
                    }
                }
                if (problemeData.getImpactDelai() != null) {
                    probleme.setImpactDelai(problemeData.getImpactDelai());
                }
                if (problemeData.getImpactCout() != null) {
                    probleme.setImpactCout(problemeData.getImpactCout());
                }
                
                return problemeRepository.save(probleme);
            });
    }

    public boolean delete(Long id) {
        return problemeRepository.findById(id)
            .map(probleme -> {
                problemeRepository.delete(probleme);
                return true;
            })
            .orElse(false);
    }

    /**
     * Detect problems from a daily report and updated operations
     */
    public List<Probleme> detectProblemsFromDailyReport(Long dailyReportId, List<Long> updatedOperationIds) {
        try {
            System.out.println("=== PROBLEM DETECTION SERVICE ===");
            System.out.println("Daily Report ID: " + dailyReportId);
            System.out.println("Updated Operation IDs: " + updatedOperationIds);
            
            // Get the daily report
            Optional<DailyReport> dailyReportOpt = dailyReportService.findById(dailyReportId);
            if (!dailyReportOpt.isPresent()) {
                System.err.println("Daily report not found: " + dailyReportId);
                return List.of();
            }
            
            DailyReport dailyReport = dailyReportOpt.get();
            System.out.println("Found daily report: " + dailyReport.getReportName());
            System.out.println("Daily report current phase: " + (dailyReport.getCurrentPhase() != null ? dailyReport.getCurrentPhase().getId() : "null"));
            
            // Get the updated operations
            List<Operation> updatedOperations = new ArrayList<>();
            if (updatedOperationIds != null && !updatedOperationIds.isEmpty()) {
                System.out.println("Fetching updated operations...");
                for (Long operationId : updatedOperationIds) {
                    Optional<Operation> operationOpt = operationRepository.findById(operationId);
                    if (operationOpt.isPresent()) {
                        Operation operation = operationOpt.get();
                        System.out.println("Operation " + operationId + " - coutPrev: " + operation.getCoutPrev() + ", coutReel: " + operation.getCoutReel());
                        updatedOperations.add(operation);
                    } else {
                        System.err.println("Operation not found: " + operationId);
                    }
                }
            }
            System.out.println("Found " + updatedOperations.size() + " updated operations");
            
            // Use the detection service to find problems
            List<Probleme> detectedProblems = problemeDetectionService.detectAndCreateProblems(dailyReport, updatedOperations);
            
            System.out.println("Detection completed. Found " + detectedProblems.size() + " problems");
            for (Probleme problem : detectedProblems) {
                System.out.println("Problem: " + problem.getType() + " - " + problem.getDescription());
                System.out.println("Impact Cost: " + problem.getImpactCout());
                System.out.println("Severity: " + problem.getGravite());
                System.out.println("Status: " + problem.getStatut());
            }
            
            return detectedProblems;
        } catch (Exception e) {
            System.err.println("Error in detectProblemsFromDailyReport: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
}

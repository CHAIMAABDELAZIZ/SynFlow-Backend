package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Operation;
import com.example.backend.repository.DailyReportRepository;
import com.example.backend.repository.OperationRepository;
import com.example.backend.repository.PhaseRepository;
import com.example.backend.repository.TypeOperationRepository;
import com.example.backend.repository.UtilisateurRepository;

@Service
@Transactional
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;
    
    @Autowired
    private PhaseRepository phaseRepository;
    
    @Autowired
    private TypeOperationRepository typeOperationRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private DailyReportRepository dailyReportRepository;

    public List<Operation> findAll() {
        return operationRepository.findAll();
    }

    public Optional<Operation> findById(Long id) {
        return operationRepository.findById(id);
    }
    
    public List<Operation> findByPhase(Long phaseId) {
        return phaseRepository.findById(phaseId)
                .map(phase -> operationRepository.findByPhase(phase))
                .orElse(List.of());
    }
    
    public List<Operation> findByTypeOperation(String typeOperationCode) {
        return typeOperationRepository.findById(typeOperationCode)
                .map(typeOperation -> operationRepository.findByTypeOperation(typeOperation))
                .orElse(List.of());
    }
    
    public List<Operation> findByCreatedBy(Long utilisateurId) {
        return utilisateurRepository.findById(utilisateurId)
                .map(utilisateur -> operationRepository.findByCreatedBy(utilisateur))
                .orElse(List.of());
    }
    
    public List<Operation> findByStatut(String statutLabel) {
        try {
            Operation.Statut statut = Operation.Statut.fromLabel(statutLabel);
            return operationRepository.findByStatut(statut);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }
    
    public List<Operation> findByDailyReport(Long dailyReportId) {
        return dailyReportRepository.findById(dailyReportId)
                .map(dailyReport -> operationRepository.findByDailyReport(dailyReport))
                .orElse(List.of());
    }

    public Operation create(Operation operation) {
        // Validate and set phase
        if (operation.getPhase() != null && operation.getPhase().getId() != null) {
            phaseRepository.findById(operation.getPhase().getId())
                    .ifPresent(operation::setPhase);
        }
        
        // Validate and set type operation
        if (operation.getTypeOperation() != null && operation.getTypeOperation().getCode() != null) {
            typeOperationRepository.findById(operation.getTypeOperation().getCode())
                    .ifPresent(operation::setTypeOperation);
        }
        
        // Validate and set created by
        if (operation.getCreatedBy() != null && operation.getCreatedBy().getId() != null) {
            utilisateurRepository.findById(operation.getCreatedBy().getId())
                    .ifPresent(operation::setCreatedBy);
        }
        
        // Validate and set daily report
        if (operation.getDailyReport() != null && operation.getDailyReport().getId() != null) {
            dailyReportRepository.findById(operation.getDailyReport().getId())
                    .ifPresent(operation::setDailyReport);
        }
        
        // Set default values
        if (operation.getCreatedAt() == null) {
            operation.setCreatedAt(LocalDateTime.now());
        }
        
        if (operation.getStatut() == null) {
            operation.setStatut(Operation.Statut.PLANIFIE);
        }
        
        if (operation.getCoutReel() == null) {
            operation.setCoutReel(0.0);
        }

        System.out.println("Creating operation with coutPrev: " + operation.getCoutPrev() + ", coutReel: " + operation.getCoutReel());
        Operation saved = operationRepository.save(operation);
        System.out.println("Saved operation with ID: " + saved.getId() + ", coutPrev: " + saved.getCoutPrev() + ", coutReel: " + saved.getCoutReel());
        
        return saved;
    }

    public Optional<Operation> update(Long id, Operation operationData) {
        return operationRepository.findById(id)
            .map(operation -> {
                System.out.println("=== UPDATING OPERATION " + id + " ===");
                System.out.println("Before update - coutPrev: " + operation.getCoutPrev() + ", coutReel: " + operation.getCoutReel());
                
                // Handle Phase relationship
                if (operationData.getPhase() != null && operationData.getPhase().getId() != null) {
                    phaseRepository.findById(operationData.getPhase().getId())
                            .ifPresent(operation::setPhase);
                }
                
                // Handle TypeOperation relationship
                if (operationData.getTypeOperation() != null && operationData.getTypeOperation().getCode() != null) {
                    typeOperationRepository.findById(operationData.getTypeOperation().getCode())
                            .ifPresent(operation::setTypeOperation);
                }
                
                // Handle CreatedBy relationship
                if (operationData.getCreatedBy() != null && operationData.getCreatedBy().getId() != null) {
                    utilisateurRepository.findById(operationData.getCreatedBy().getId())
                            .ifPresent(operation::setCreatedBy);
                }
                
                // Handle DailyReport relationship
                if (operationData.getDailyReport() != null && operationData.getDailyReport().getId() != null) {
                    dailyReportRepository.findById(operationData.getDailyReport().getId())
                            .ifPresent(operation::setDailyReport);
                }
                
                if (operationData.getDescription() != null) {
                    operation.setDescription(operationData.getDescription());
                }
                if (operationData.getStatut() != null) {
                    operation.setStatut(operationData.getStatut());
                }
                if (operationData.getCoutPrev() != null) {
                    operation.setCoutPrev(operationData.getCoutPrev());
                }
                if (operationData.getCoutReel() != null) {
                    operation.setCoutReel(operationData.getCoutReel());
                    System.out.println("Updated coutReel to: " + operationData.getCoutReel());
                }
                
                Operation saved = operationRepository.save(operation);
                System.out.println("After update - coutPrev: " + saved.getCoutPrev() + ", coutReel: " + saved.getCoutReel());
                System.out.println("=== OPERATION UPDATE COMPLETE ===");
                
                return saved;
            });
    }

    public boolean delete(Long id) {
        return operationRepository.findById(id)
            .map(operation -> {
                operationRepository.delete(operation);
                return true;
            })
            .orElse(false);
    }
}

package com.example.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Operation;
import com.example.backend.model.Phase;
import com.example.backend.repository.OperationRepository;
import com.example.backend.repository.PhaseRepository;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private PhaseRepository phaseRepository;
    
    @Autowired
    private OperationRepository operationRepository;

    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // 1. Costs in DZD per phase for the Card
        dashboardData.put("phaseCosts", getPhaseCosts());
        
        // 2. Total phases cost for the MapCard
        dashboardData.put("totalPhasesCost", getTotalPhasesCost());
        
        // 3. Total dépassement de délai ou de coût for FlowChart
        dashboardData.put("overruns", getOverruns());
        
        // 4. Most costly operations by TypeOperation for CostChart
        dashboardData.put("costlyOperations", getMostCostlyOperations());
        
        return dashboardData;
    }

    private List<Map<String, Object>> getPhaseCosts() {
        List<Phase> phases = phaseRepository.findAll();
        List<Map<String, Object>> phaseCosts = new ArrayList<>();
        
        for (Phase phase : phases) {
            List<Operation> phaseOperations = operationRepository.findByPhase(phase);
            
            double plannedCost = phaseOperations.stream()
                .mapToDouble(op -> op.getCoutPrev() != null ? op.getCoutPrev() : 0.0)
                .sum();
                
            double actualCost = phaseOperations.stream()
                .mapToDouble(op -> op.getCoutReel() != null ? op.getCoutReel() : 0.0)
                .sum();
            
            Map<String, Object> phaseData = new HashMap<>();
            phaseData.put("phaseNumber", phase.getNumeroPhase());
            phaseData.put("phaseName", "Phase " + phase.getNumeroPhase() + " - " + 
                getDiametreLabel(phase.getDiametre()));
            phaseData.put("plannedCost", plannedCost);
            phaseData.put("actualCost", actualCost);
            phaseData.put("wellName", phase.getForage() != null && phase.getForage().getPuit() != null ? 
                phase.getForage().getPuit().getNom() : "Unknown");
            
            phaseCosts.add(phaseData);
        }
        
        return phaseCosts;
    }

    private Map<String, Object> getTotalPhasesCost() {
        List<Phase> phases = phaseRepository.findAll();
        double totalPlannedCost = 0.0;
        double totalActualCost = 0.0;
        int totalPhases = phases.size();
        
        for (Phase phase : phases) {
            List<Operation> phaseOperations = operationRepository.findByPhase(phase);
            
            totalPlannedCost += phaseOperations.stream()
                .mapToDouble(op -> op.getCoutPrev() != null ? op.getCoutPrev() : 0.0)
                .sum();
                
            totalActualCost += phaseOperations.stream()
                .mapToDouble(op -> op.getCoutReel() != null ? op.getCoutReel() : 0.0)
                .sum();
        }
        
        Map<String, Object> totalCosts = new HashMap<>();
        totalCosts.put("totalPlannedCost", totalPlannedCost);
        totalCosts.put("totalActualCost", totalActualCost);
        totalCosts.put("totalPhases", totalPhases);
        totalCosts.put("costOverrun", totalActualCost - totalPlannedCost);
        totalCosts.put("costOverrunPercentage", totalPlannedCost > 0 ? 
            ((totalActualCost - totalPlannedCost) / totalPlannedCost) * 100 : 0);
        
        return totalCosts;
    }

    private Map<String, Object> getOverruns() {
        List<Phase> phases = phaseRepository.findAll();
        int timeOverruns = 0;
        int costOverruns = 0;
        double totalTimeOverrunDays = 0.0;
        double totalCostOverrun = 0.0;
        
        LocalDate today = LocalDate.now();
        
        for (Phase phase : phases) {
            // Check time overruns
            if (phase.getDateFinPrevue() != null) {
                LocalDate plannedEnd = phase.getDateFinPrevue();
                LocalDate actualEnd = phase.getDateFinReelle() != null ? 
                    phase.getDateFinReelle() : today;
                
                if (actualEnd.isAfter(plannedEnd)) {
                    timeOverruns++;
                    totalTimeOverrunDays += actualEnd.toEpochDay() - plannedEnd.toEpochDay();
                }
            }
            
            // Check cost overruns
            List<Operation> phaseOperations = operationRepository.findByPhase(phase);
            double plannedCost = phaseOperations.stream()
                .mapToDouble(op -> op.getCoutPrev() != null ? op.getCoutPrev() : 0.0)
                .sum();
            double actualCost = phaseOperations.stream()
                .mapToDouble(op -> op.getCoutReel() != null ? op.getCoutReel() : 0.0)
                .sum();
            
            if (actualCost > plannedCost) {
                costOverruns++;
                totalCostOverrun += (actualCost - plannedCost);
            }
        }
        
        Map<String, Object> overruns = new HashMap<>();
        overruns.put("timeOverruns", timeOverruns);
        overruns.put("costOverruns", costOverruns);
        overruns.put("totalTimeOverrunDays", totalTimeOverrunDays);
        overruns.put("totalCostOverrun", totalCostOverrun);
        overruns.put("totalPhases", phases.size());
        
        return overruns;
    }

    private List<Map<String, Object>> getMostCostlyOperations() {
        List<Operation> operations = operationRepository.findAll();
        
        // Group by TypeOperation and sum costs
        Map<String, Double> operationTypeCosts = operations.stream()
            .filter(op -> op.getTypeOperation() != null)
            .collect(Collectors.groupingBy(
                op -> op.getTypeOperation().getNom(),
                Collectors.summingDouble(op -> 
                    (op.getCoutReel() != null ? op.getCoutReel() : 0.0) +
                    (op.getCoutPrev() != null ? op.getCoutPrev() : 0.0)
                )
            ));
        
        // Convert to list and sort by cost (descending)
        List<Map<String, Object>> costlyOperations = operationTypeCosts.entrySet().stream()
            .map(entry -> {
                Map<String, Object> operationData = new HashMap<>();
                operationData.put("operationType", entry.getKey());
                operationData.put("totalCost", entry.getValue());
                
                // Calculate additional metrics
                List<Operation> typeOperations = operations.stream()
                    .filter(op -> op.getTypeOperation() != null && 
                        op.getTypeOperation().getNom().equals(entry.getKey()))
                    .collect(Collectors.toList());
                
                double totalPlanned = typeOperations.stream()
                    .mapToDouble(op -> op.getCoutPrev() != null ? op.getCoutPrev() : 0.0)
                    .sum();
                
                double totalActual = typeOperations.stream()
                    .mapToDouble(op -> op.getCoutReel() != null ? op.getCoutReel() : 0.0)
                    .sum();
                
                operationData.put("plannedCost", totalPlanned);
                operationData.put("actualCost", totalActual);
                operationData.put("operationCount", typeOperations.size());
                operationData.put("overrunPercentage", totalPlanned > 0 ? 
                    ((totalActual - totalPlanned) / totalPlanned) * 100 : 0);
                
                return operationData;
            })
            .sorted((a, b) -> Double.compare((Double) b.get("totalCost"), (Double) a.get("totalCost")))
            .limit(10) // Top 10 most costly
            .collect(Collectors.toList());
        
        return costlyOperations;
    }

    private String getDiametreLabel(Phase.Diametre diametre) {
        if (diametre == null) return "Unknown";
        
        switch (diametre) {
            case POUCES_26: return "26\"";
            case POUCES_16: return "16\"";
            case POUCES_12_25: return "12 1/4\"";
            case POUCES_8_5: return "8 1/2\"";
            default: return diametre.toString();
        }
    }
}

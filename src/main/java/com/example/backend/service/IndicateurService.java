package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Indicateur;
import com.example.backend.model.Operation;
import com.example.backend.model.TypeIndicateur;
import com.example.backend.repository.IndicateurRepository;
import com.example.backend.repository.OperationRepository;
import com.example.backend.repository.TypeIndicateurRepository;

@Service
@Transactional
public class IndicateurService {

    @Autowired
    private IndicateurRepository indicateurRepository;
    
    @Autowired
    private OperationRepository operationRepository;
    
    @Autowired
    private TypeIndicateurRepository typeIndicateurRepository;

    public List<Indicateur> findAll() {
        return indicateurRepository.findAll();
    }

    public Optional<Indicateur> findById(Long id) {
        return indicateurRepository.findById(id);
    }
    
    public List<Indicateur> findByOperation(Long operationId) {
        return operationRepository.findById(operationId)
                .map(operation -> indicateurRepository.findByOperation(operation))
                .orElse(List.of());
    }
    
    public List<Indicateur> findByTypeIndicateur(Long typeIndicateurId) {
        return typeIndicateurRepository.findById(typeIndicateurId)
                .map(typeIndicateur -> indicateurRepository.findByTypeIndicateur(typeIndicateur))
                .orElse(List.of());
    }
    
    public List<Indicateur> findByOperationAndTypeIndicateur(Long operationId, Long typeIndicateurId) {
        Optional<Operation> operationOpt = operationRepository.findById(operationId);
        Optional<TypeIndicateur> typeIndicateurOpt = typeIndicateurRepository.findById(typeIndicateurId);
        
        if (operationOpt.isPresent() && typeIndicateurOpt.isPresent()) {
            return indicateurRepository.findByOperationAndTypeIndicateur(
                    operationOpt.get(), typeIndicateurOpt.get());
        }
        return List.of();
    }

    public Indicateur create(Indicateur indicateur) {
        if (indicateur.getDateMesure() == null) {
            indicateur.setDateMesure(LocalDateTime.now());
        }
        return indicateurRepository.save(indicateur);
    }

    public Optional<Indicateur> update(Long id, Indicateur indicateurData) {
        return indicateurRepository.findById(id)
            .map(indicateur -> {
                // Handle Operation relationship
                if (indicateurData.getOperation() != null && indicateurData.getOperation().getId() != null) {
                    operationRepository.findById(indicateurData.getOperation().getId())
                        .ifPresent(indicateur::setOperation);
                }
                
                // Handle TypeIndicateur relationship
                if (indicateurData.getTypeIndicateur() != null && indicateurData.getTypeIndicateur().getId() != null) {
                    typeIndicateurRepository.findById(indicateurData.getTypeIndicateur().getId())
                        .ifPresent(indicateur::setTypeIndicateur);
                }
                
                if (indicateurData.getValeurPrevue() != null) {
                    indicateur.setValeurPrevue(indicateurData.getValeurPrevue());
                }
                if (indicateurData.getValeurReelle() != null) {
                    indicateur.setValeurReelle(indicateurData.getValeurReelle());
                }
                if (indicateurData.getDateMesure() != null) {
                    indicateur.setDateMesure(indicateurData.getDateMesure());
                }
                if (indicateurData.getCommentaire() != null) {
                    indicateur.setCommentaire(indicateurData.getCommentaire());
                }
                
                return indicateurRepository.save(indicateur);
            });
    }

    public boolean delete(Long id) {
        return indicateurRepository.findById(id)
            .map(indicateur -> {
                indicateurRepository.delete(indicateur);
                return true;
            })
            .orElse(false);
    }
}

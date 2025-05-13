package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.TypeIndicateur;
import com.example.backend.repository.TypeIndicateurRepository;
import com.example.backend.repository.TypeOperationRepository;

@Service
@Transactional
public class TypeIndicateurService {

    @Autowired
    private TypeIndicateurRepository typeIndicateurRepository;
    
    @Autowired
    private TypeOperationRepository typeOperationRepository;

    public List<TypeIndicateur> findAll() {
        return typeIndicateurRepository.findAll();
    }

    public Optional<TypeIndicateur> findById(Long id) {
        return typeIndicateurRepository.findById(id);
    }
    
    public List<TypeIndicateur> findByTypeOperation(String typeOperationCode) {
        return typeOperationRepository.findById(typeOperationCode)
                .map(typeOperation -> typeIndicateurRepository.findByTypeOperation(typeOperation))
                .orElse(List.of());
    }
    
    public List<TypeIndicateur> findByEstObligatoire(Boolean estObligatoire) {
        return typeIndicateurRepository.findByEstObligatoire(estObligatoire);
    }

    public TypeIndicateur create(TypeIndicateur typeIndicateur) {
        return typeIndicateurRepository.save(typeIndicateur);
    }

    public Optional<TypeIndicateur> update(Long id, TypeIndicateur typeIndicateurData) {
        return typeIndicateurRepository.findById(id)
            .map(typeIndicateur -> {
                if (typeIndicateurData.getNom() != null) {
                    typeIndicateur.setNom(typeIndicateurData.getNom());
                }
                if (typeIndicateurData.getUnite() != null) {
                    typeIndicateur.setUnite(typeIndicateurData.getUnite());
                }
                
                // Handle TypeOperation relationship
                if (typeIndicateurData.getTypeOperation() != null && 
                    typeIndicateurData.getTypeOperation().getCode() != null) {
                    typeOperationRepository.findById(typeIndicateurData.getTypeOperation().getCode())
                        .ifPresent(typeIndicateur::setTypeOperation);
                }
                
                if (typeIndicateurData.getEstObligatoire() != null) {
                    typeIndicateur.setEstObligatoire(typeIndicateurData.getEstObligatoire());
                }
                
                return typeIndicateurRepository.save(typeIndicateur);
            });
    }

    public boolean delete(Long id) {
        return typeIndicateurRepository.findById(id)
            .map(typeIndicateur -> {
                typeIndicateurRepository.delete(typeIndicateur);
                return true;
            })
            .orElse(false);
    }
}

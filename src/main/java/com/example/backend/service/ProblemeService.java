package com.example.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (probleme.getDateDetection() == null) {
            probleme.setDateDetection(LocalDate.now());
        }
        
        // Default to OUVERT status if not specified
        if (probleme.getStatut() == null) {
            probleme.setStatut(Probleme.Statut.OUVERT);
        }
        
        return problemeRepository.save(probleme);
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
}

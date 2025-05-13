package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Document;
import com.example.backend.repository.DocumentRepository;
import com.example.backend.repository.OperationRepository;
import com.example.backend.repository.PhaseRepository;
import com.example.backend.repository.PuitRepository;
import com.example.backend.repository.UtilisateurRepository;

@Service
@Transactional
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private PuitRepository puitRepository;
    
    @Autowired
    private PhaseRepository phaseRepository;
    
    @Autowired
    private OperationRepository operationRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Optional<Document> findById(Long id) {
        return documentRepository.findById(id);
    }
    
    public List<Document> findByPuit(Long puitId) {
        return puitRepository.findById(puitId)
                .map(puit -> documentRepository.findByPuit(puit))
                .orElse(List.of());
    }
    
    public List<Document> findByPhase(Long phaseId) {
        return phaseRepository.findById(phaseId)
                .map(phase -> documentRepository.findByPhase(phase))
                .orElse(List.of());
    }
    
    public List<Document> findByOperation(Long operationId) {
        return operationRepository.findById(operationId)
                .map(operation -> documentRepository.findByOperation(operation))
                .orElse(List.of());
    }
    
    public List<Document> findByUploadePar(Long utilisateurId) {
        return utilisateurRepository.findById(utilisateurId)
                .map(utilisateur -> documentRepository.findByUploadePar(utilisateur))
                .orElse(List.of());
    }
    
    public List<Document> findByEstPublic(Boolean estPublic) {
        return documentRepository.findByEstPublic(estPublic);
    }
    
    public List<Document> findByType(String type) {
        return documentRepository.findByType(type);
    }

    public Document create(Document document) {
        if (document.getDateUpload() == null) {
            document.setDateUpload(LocalDateTime.now());
        }
        
        // Default to private if not specified
        if (document.getEstPublic() == null) {
            document.setEstPublic(false);
        }
        
        return documentRepository.save(document);
    }

    public Optional<Document> update(Long id, Document documentData) {
        return documentRepository.findById(id)
            .map(document -> {
                // Handle Puit relationship
                if (documentData.getPuit() != null && documentData.getPuit().getId() != null) {
                    puitRepository.findById(documentData.getPuit().getId())
                        .ifPresent(document::setPuit);
                }
                
                // Handle Phase relationship
                if (documentData.getPhase() != null && documentData.getPhase().getId() != null) {
                    phaseRepository.findById(documentData.getPhase().getId())
                        .ifPresent(document::setPhase);
                }
                
                // Handle Operation relationship
                if (documentData.getOperation() != null && documentData.getOperation().getId() != null) {
                    operationRepository.findById(documentData.getOperation().getId())
                        .ifPresent(document::setOperation);
                }
                
                if (documentData.getNom() != null) {
                    document.setNom(documentData.getNom());
                }
                if (documentData.getType() != null) {
                    document.setType(documentData.getType());
                }
                if (documentData.getChemin() != null) {
                    document.setChemin(documentData.getChemin());
                }
                if (documentData.getTaille() != null) {
                    document.setTaille(documentData.getTaille());
                }
                // Don't update upload date and uploader
                if (documentData.getDescription() != null) {
                    document.setDescription(documentData.getDescription());
                }
                if (documentData.getEstPublic() != null) {
                    document.setEstPublic(documentData.getEstPublic());
                }
                
                return documentRepository.save(document);
            });
    }

    public boolean delete(Long id) {
        return documentRepository.findById(id)
            .map(document -> {
                documentRepository.delete(document);
                return true;
            })
            .orElse(false);
    }
}

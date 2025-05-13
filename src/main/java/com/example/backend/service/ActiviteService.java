package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Activite;
import com.example.backend.repository.ActiviteRepository;
import com.example.backend.repository.UtilisateurRepository;

@Service
@Transactional
public class ActiviteService {

    @Autowired
    private ActiviteRepository activiteRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Activite> findAll() {
        return activiteRepository.findAll();
    }
    
    public Page<Activite> findAllPaginated(Pageable pageable) {
        return activiteRepository.findAll(pageable);
    }

    public Optional<Activite> findById(Long id) {
        return activiteRepository.findById(id);
    }
    
    public List<Activite> findByUtilisateur(Long utilisateurId) {
        return utilisateurRepository.findById(utilisateurId)
                .map(utilisateur -> activiteRepository.findByUtilisateur(utilisateur))
                .orElse(List.of());
    }
    
    public Page<Activite> findByUtilisateurPaginated(Long utilisateurId, Pageable pageable) {
        return utilisateurRepository.findById(utilisateurId)
                .map(utilisateur -> activiteRepository.findByUtilisateur(utilisateur, pageable))
                .orElse(Page.empty());
    }
    
    public List<Activite> findByType(String typeLabel) {
        try {
            Activite.Type type = Activite.Type.fromLabel(typeLabel);
            return activiteRepository.findByType(type);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }
    
    public List<Activite> findByDateRange(LocalDateTime debut, LocalDateTime fin) {
        return activiteRepository.findByDateBetween(debut, fin);
    }
    
    public List<Activite> findByTypeEntite(String typeEntiteLabel) {
        try {
            Activite.TypeEntite typeEntite = Activite.TypeEntite.fromLabel(typeEntiteLabel);
            return activiteRepository.findByTypeEntite(typeEntite);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }
    
    public List<Activite> findByEntiteConcerne(String entiteConcerne) {
        return activiteRepository.findByEntiteConcerne(entiteConcerne);
    }

    public Activite create(Activite activite) {
        if (activite.getDate() == null) {
            activite.setDate(LocalDateTime.now());
        }
        return activiteRepository.save(activite);
    }
    
    // Helper method to log activities
    public Activite logActivity(Long utilisateurId, String typeLabel, String description, 
                                String entiteConcerne, String typeEntiteLabel) {
        Activite activite = new Activite();
        
        utilisateurRepository.findById(utilisateurId).ifPresent(activite::setUtilisateur);
        
        try {
            activite.setType(Activite.Type.fromLabel(typeLabel));
        } catch (IllegalArgumentException e) {
            activite.setType(Activite.Type.AUTRE);
        }
        
        activite.setDescription(description);
        activite.setDate(LocalDateTime.now());
        activite.setEntiteConcerne(entiteConcerne);
        
        try {
            activite.setTypeEntite(Activite.TypeEntite.fromLabel(typeEntiteLabel));
        } catch (IllegalArgumentException e) {
            activite.setTypeEntite(Activite.TypeEntite.AUTRE);
        }
        
        return activiteRepository.save(activite);
    }

    public Optional<Activite> update(Long id, Activite activiteData) {
        return activiteRepository.findById(id)
            .map(activite -> {
                // Handle Utilisateur relationship
                if (activiteData.getUtilisateur() != null && activiteData.getUtilisateur().getId() != null) {
                    utilisateurRepository.findById(activiteData.getUtilisateur().getId())
                        .ifPresent(activite::setUtilisateur);
                }
                
                if (activiteData.getType() != null) {
                    activite.setType(activiteData.getType());
                }
                if (activiteData.getDescription() != null) {
                    activite.setDescription(activiteData.getDescription());
                }
                if (activiteData.getDate() != null) {
                    activite.setDate(activiteData.getDate());
                }
                if (activiteData.getEntiteConcerne() != null) {
                    activite.setEntiteConcerne(activiteData.getEntiteConcerne());
                }
                if (activiteData.getTypeEntite() != null) {
                    activite.setTypeEntite(activiteData.getTypeEntite());
                }
                
                return activiteRepository.save(activite);
            });
    }

    public boolean delete(Long id) {
        return activiteRepository.findById(id)
            .map(activite -> {
                activiteRepository.delete(activite);
                return true;
            })
            .orElse(false);
    }
}

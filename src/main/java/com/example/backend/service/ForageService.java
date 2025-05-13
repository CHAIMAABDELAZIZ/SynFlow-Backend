package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Forage;
import com.example.backend.repository.ForageRepository;

@Service
@Transactional
public class ForageService {

    @Autowired
    private ForageRepository forageRepository;

    public List<Forage> findAll() {
        return forageRepository.findAll();
    }

    public Optional<Forage> findById(Long id) {
        return forageRepository.findById(id);
    }

    public Forage create(Forage forage) {
        return forageRepository.save(forage);
    }

    public Optional<Forage> update(Long id, Forage forageData) {
        return forageRepository.findById(id)
            .map(forage -> {
                forage.setCout(forageData.getCout());
                forage.setDate_debut(forageData.getDate_debut());
                forage.setDate_fin(forageData.getDate_fin());
                return forageRepository.save(forage);
            });
    }

    public boolean delete(Long id) {
        return forageRepository.findById(id)
            .map(forage -> {
                forageRepository.delete(forage);
                return true;
            })
            .orElse(false);
    }
}

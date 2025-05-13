package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Reservoir;
import com.example.backend.repository.PuitRepository;
import com.example.backend.repository.ReservoirRepository;

@Service
@Transactional
public class ReservoirService {

    @Autowired
    private ReservoirRepository reservoirRepository;
    
    @Autowired
    private PuitRepository puitRepository;

    public List<Reservoir> findAll() {
        return reservoirRepository.findAll();
    }

    public Optional<Reservoir> findById(Long id) {
        return reservoirRepository.findById(id);
    }
    
    public List<Reservoir> findByPuit(Long puitId) {
        return puitRepository.findById(puitId)
                .map(puit -> reservoirRepository.findByPuit(puit))
                .orElse(List.of());
    }

    public Reservoir create(Reservoir reservoir) {
        return reservoirRepository.save(reservoir);
    }

    public Optional<Reservoir> update(Long id, Reservoir reservoirData) {
        return reservoirRepository.findById(id)
            .map(reservoir -> {
                // Handle Puit relationship
                if (reservoirData.getPuit() != null && reservoirData.getPuit().getId() != null) {
                    puitRepository.findById(reservoirData.getPuit().getId())
                        .ifPresent(reservoir::setPuit);
                }
                
                if (reservoirData.getNom() != null) {
                    reservoir.setNom(reservoirData.getNom());
                }
                if (reservoirData.getNatureFluide() != null) {
                    reservoir.setNatureFluide(reservoirData.getNatureFluide());
                }
                if (reservoirData.getHauteurUtile() != null) {
                    reservoir.setHauteurUtile(reservoirData.getHauteurUtile());
                }
                if (reservoirData.getContactFluide() != null) {
                    reservoir.setContactFluide(reservoirData.getContactFluide());
                }
                if (reservoirData.getNetPay() != null) {
                    reservoir.setNetPay(reservoirData.getNetPay());
                }
                if (reservoirData.getDebit() != null) {
                    reservoir.setDebit(reservoirData.getDebit());
                }
                if (reservoirData.getPressionTete() != null) {
                    reservoir.setPressionTete(reservoirData.getPressionTete());
                }
                
                return reservoirRepository.save(reservoir);
            });
    }

    public boolean delete(Long id) {
        return reservoirRepository.findById(id)
            .map(reservoir -> {
                reservoirRepository.delete(reservoir);
                return true;
            })
            .orElse(false);
    }
}

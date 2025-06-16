package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.DailyReport;
import com.example.backend.model.Operation;
import com.example.backend.model.Phase;
import com.example.backend.model.TypeOperation;
import com.example.backend.model.Utilisateur;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByPhase(Phase phase);
    List<Operation> findByTypeOperation(TypeOperation typeOperation);
    List<Operation> findByCreatedBy(Utilisateur createdBy);
    List<Operation> findByStatut(Operation.Statut statut);
    List<Operation> findByDailyReport(DailyReport dailyReport);
}

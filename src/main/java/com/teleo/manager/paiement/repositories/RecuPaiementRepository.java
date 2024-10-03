package com.teleo.manager.paiement.repositories;

import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.paiement.dto.request.RecuPaiementRequest;
import com.teleo.manager.paiement.entities.RecuPaiement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecuPaiementRepository extends GenericRepository<RecuPaiementRequest, RecuPaiement> {

    @Query("SELECT DISTINCT r FROM RecuPaiement r LEFT JOIN FETCH r.paiement p WHERE p.id = :paiementId")
    List<RecuPaiement> findByPaiementId(@Param("paiementId") Long paiementId);

    @Query("SELECT DISTINCT r FROM RecuPaiement r WHERE r.numeroRecu = :numeroRecu")
    Optional<RecuPaiement> findByNumeroRecu(@Param("numeroRecu") String numeroRecu);

    @Query("SELECT DISTINCT r FROM RecuPaiement r WHERE r.dateEmission BETWEEN :startDate AND :endDate")
    List<RecuPaiement> findAllByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT DISTINCT r FROM RecuPaiement r WHERE r.montant BETWEEN :minMontant AND :maxMontant")
    List<RecuPaiement> findAllByMontantRange(@Param("minMontant") BigDecimal minMontant, @Param("maxMontant") BigDecimal maxMontant);
}

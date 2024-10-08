package com.teleo.manager.paiement.repositories;

import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.paiement.dto.request.PaiementRequest;
import com.teleo.manager.paiement.entities.Paiement;
import com.teleo.manager.paiement.enums.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository extends GenericRepository<PaiementRequest, Paiement> {

    @Query("SELECT SUM(p.montant) FROM Paiement p " +
            "JOIN p.souscription s " +
            "JOIN s.police pol " +
            "JOIN pol.garanties g " +
            "WHERE s.id = :souscriptionId AND g.id = :garantieId")
    BigDecimal sumTotalPaiementsBySouscriptionAndGarantie(@Param("souscriptionId") Long souscriptionId,
                                                          @Param("garantieId") Long garantieId);

    @Query("SELECT p FROM Paiement p WHERE p.souscription = :souscription AND p.type IN :types")
    List<Paiement> findBySouscriptionAndTypeIn(@Param("souscription") Souscription souscription,
                                               @Param("types") List<PaymentType> types);

    @Query("SELECT DISTINCT p FROM Paiement p WHERE p.numeroPaiement = :numeroPaiement")
    Optional<Paiement> findByNumeroPaiement(@Param("numeroPaiement") String numeroPaiement);

    @Query("SELECT DISTINCT p FROM Paiement p WHERE p.datePaiement BETWEEN :startDate AND :endDate")
    List<Paiement> findAllByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT DISTINCT p FROM Paiement p LEFT JOIN FETCH p.souscription s WHERE s.id = :souscriptionId")
    List<Paiement> findAllBySouscriptionId(@Param("souscriptionId") Long souscriptionId);

    @Query("SELECT DISTINCT p FROM Paiement p WHERE p.reclamation.id = :reclamationId")
    List<Paiement> findAllByReclamationId(@Param("reclamationId") Long reclamationId);

    @Query("SELECT DISTINCT p FROM Paiement p LEFT JOIN FETCH p.recuPaiements r WHERE r.id = :recuPaiementId")
    Optional<Paiement> findByRecuPaiementId(@Param("recuPaiementId") Long recuPaiementId);

    @Query("SELECT DISTINCT p FROM Paiement p WHERE p.souscription.id = :souscriptionId AND p.datePaiement BETWEEN :startDate AND :endDate AND p.type = 'PRIME'")
    List<Paiement> findAllBySouscriptionAndBetween(@Param("souscriptionId") Long souscriptionId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT DISTINCT p FROM Paiement p WHERE p.reclamation.id = :reclamationId AND p.datePaiement BETWEEN :startDate AND :endDate AND p.type IN ('REMBOURSEMENT', 'PRESTATION')")
    List<Paiement> findAllByReclamationAndBetween(@Param("reclamationId") Long reclamationId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT DISTINCT p FROM Paiement p WHERE p.souscription.id = :souscriptionId AND p.type = 'PRIME' ORDER BY p.datePaiement DESC")
    Page<Paiement> findLastPrimePaiementBySouscriptionId(@Param("souscriptionId") Long souscriptionId, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Paiement p WHERE p.souscription.id = :souscriptionId AND p.type IN ('REMBOURSEMENT', 'PRESTATION') ORDER BY p.datePaiement DESC")
    Page<Paiement> findLastOtherPaiementBySouscriptionId(@Param("souscriptionId") Long souscriptionId, Pageable pageable);
}

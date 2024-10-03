package com.teleo.manager.assurance.repositories;

import com.teleo.manager.assurance.dto.request.SouscriptionRequest;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.assurance.enums.SouscriptionStatus;
import com.teleo.manager.generic.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SouscriptionRepository extends GenericRepository<SouscriptionRequest, Souscription> {

    @Query("SELECT DISTINCT s FROM Souscription s WHERE s.status = :status")
    List<Souscription> findSouscriptionsStatusIsWaiting(@Param("status") SouscriptionStatus status);

    @Query("SELECT DISTINCT s FROM Souscription s WHERE s.dateExpiration <= :date")
    List<Souscription> findSouscriptionsExpiringSoon(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT s FROM Souscription s WHERE s.assure.id = :assureId")
    List<Souscription> findAllByAssureId(@Param("assureId") Long assureId);

    @Query("SELECT DISTINCT s FROM Souscription s WHERE s.police.id = :policeId")
    List<Souscription> findAllByPoliceId(@Param("policeId") Long policeId);

    @Query("SELECT DISTINCT s FROM Souscription s LEFT JOIN FETCH s.contrats c WHERE c.id = :contratId")
    Optional<Souscription> findWithContratsById(@Param("contratId") Long contratId);

    @Query("SELECT DISTINCT s FROM Souscription s LEFT JOIN FETCH s.sinistres e WHERE e.id = :sinistreId")
    Optional<Souscription> findWithSinistresById(@Param("sinistreId") Long sinistreId);

    @Query("SELECT DISTINCT s FROM Souscription s LEFT JOIN FETCH s.prestations p WHERE p.id = :prestationId")
    Optional<Souscription> findWithPrestationsById(@Param("prestationId") Long prestationId);

    @Query("SELECT DISTINCT s FROM Souscription s LEFT JOIN FETCH s.paiements p WHERE p.id = :paiementId")
    Optional<Souscription> findWithPaiementsById(@Param("paiementId") Long paiementId);

    @Query("SELECT DISTINCT s FROM Souscription s LEFT JOIN FETCH s.reclamations r WHERE r.id = :reclamationId")
    Optional<Souscription> findWithReclamationsById(@Param("reclamationId") Long reclamationId);

    @Query("SELECT DISTINCT s FROM Souscription s WHERE s.dateExpiration > :date")
    List<Souscription> findSouscriptionsIsNotExpirate(@Param("date") LocalDate date);
}

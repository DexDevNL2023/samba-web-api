package com.teleo.manager.sinistre.repositories;

import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.sinistre.dto.request.ReclamationRequest;
import com.teleo.manager.sinistre.entities.Reclamation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReclamationRepository extends GenericRepository<ReclamationRequest, Reclamation> {

    @Query("SELECT DISTINCT r FROM Reclamation r LEFT JOIN FETCH r.souscription s WHERE s.id = :souscriptionId")
    List<Reclamation> findAllBySouscriptionId(@Param("souscriptionId") Long souscriptionId);

    @Query("SELECT DISTINCT r FROM Reclamation r LEFT JOIN FETCH r.sinistre s WHERE s.id = :sinistreId")
    List<Reclamation> findAllBySinistreId(@Param("sinistreId") Long sinistreId);

    @Query("SELECT DISTINCT r FROM Reclamation r LEFT JOIN FETCH r.prestation p WHERE p.id = :prestationId")
    List<Reclamation> findAllByPrestationId(@Param("prestationId") Long prestationId);

    @Query("SELECT DISTINCT r FROM Reclamation r LEFT JOIN FETCH r.paiements p WHERE p.id = :paiementId")
    Optional<Reclamation> findWithPaiementsById(@Param("paiementId") Long paiementId);
}

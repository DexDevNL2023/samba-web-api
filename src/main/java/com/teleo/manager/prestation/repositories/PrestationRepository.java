package com.teleo.manager.prestation.repositories;

import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.prestation.dto.request.PrestationRequest;
import com.teleo.manager.prestation.entities.Prestation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestationRepository extends GenericRepository<PrestationRequest, Prestation> {

    @Query("SELECT DISTINCT p FROM Prestation p LEFT JOIN FETCH p.fournisseur f WHERE f.id = :fournisseurId")
    List<Prestation> findAllByFournisseurId(@Param("fournisseurId") Long fournisseurId);

    @Query("SELECT DISTINCT p FROM Prestation p LEFT JOIN FETCH p.souscription s WHERE s.id = :souscriptionId")
    List<Prestation> findAllBySouscriptionId(@Param("souscriptionId") Long souscriptionId);

    @Query("SELECT DISTINCT p FROM Prestation p LEFT JOIN FETCH p.sinistre s WHERE s.id = :sinistreId")
    List<Prestation> findAllWithSinistreById(@Param("sinistreId") Long sinistreId);

    @Query("SELECT DISTINCT p FROM Prestation p LEFT JOIN FETCH p.financeurs f WHERE p.id = :prestationId")
    List<Prestation> findWithFinanceursById(@Param("prestationId") Long prestationId);

    @Query("SELECT DISTINCT p FROM Prestation p LEFT JOIN FETCH p.documents d WHERE d.id = :documentId")
    Optional<Prestation> findByIdWithDocuments(@Param("documentId") Long documentId);
}

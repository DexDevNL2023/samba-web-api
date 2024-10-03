package com.teleo.manager.sinistre.repositories;

import com.teleo.manager.sinistre.dto.request.SinistreRequest;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.generic.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SinistreRepository extends GenericRepository<SinistreRequest, Sinistre> {

    @Query("SELECT DISTINCT s FROM Sinistre s LEFT JOIN FETCH s.souscription e WHERE e.id = :souscriptionId")
    List<Sinistre> findAllBySouscriptionId(@Param("souscriptionId") Long souscriptionId);

    @Query("SELECT DISTINCT s FROM Sinistre s LEFT JOIN FETCH s.prestations p WHERE p.id = :prestationId")
    Optional<Sinistre> findByIdWithPrestations(@Param("prestationId") Long prestationId);

    @Query("SELECT DISTINCT s FROM Sinistre s LEFT JOIN FETCH s.documents d WHERE d.id = :documentId")
    Optional<Sinistre> findByIdWithDocuments(@Param("documentId") Long documentId);
}

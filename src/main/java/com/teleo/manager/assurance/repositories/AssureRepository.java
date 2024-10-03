package com.teleo.manager.assurance.repositories;

import com.teleo.manager.assurance.dto.request.AssureRequest;
import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.generic.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssureRepository extends GenericRepository<AssureRequest, Assure> {

    @Query("SELECT DISTINCT a FROM Assure a LEFT JOIN FETCH a.dossiers d WHERE d.id = :dossierId")
    Optional<Assure> findAssureByDossierId(@Param("dossierId") Long dossierId);

    @Query("SELECT DISTINCT a FROM Assure a LEFT JOIN FETCH a.souscriptions s WHERE s.id = :souscriptionId")
    Optional<Assure> findAssureBySouscriptionId(@Param("souscriptionId") Long souscriptionId);

    // Méthode ajoutée
    @Query("SELECT DISTINCT a FROM Assure a WHERE a.account.id = :accountId")
    Optional<Assure> findAssureByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT DISTINCT a FROM Assure a WHERE " +
            "(a.updatedAt >= :timeLimit) AND " +
            "(a.numNiu IS NULL OR a.dateNaissance IS NULL OR a.numCni IS NULL OR a.email IS NULL OR a.telephone IS NULL OR a.signature IS NULL " +
            "OR a.numNiu <> a.oldNumNiu OR a.dateNaissance <> a.oldDateNaissance OR a.numCni <> a.oldNumCni OR a.email <> a.oldEmail OR a.telephone <> a.oldTelephone OR a.signature <> a.oldSignature)")
    List<Assure> findAssuresWithUpdatedOrEmptyFields(@Param("timeLimit") Instant timeLimit);
}


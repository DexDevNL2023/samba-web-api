package com.teleo.manager.assurance.repositories;

import com.teleo.manager.assurance.dto.request.PoliceAssuranceRequest;
import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.assurance.entities.PoliceAssurance;
import com.teleo.manager.assurance.enums.GarantieStatus;
import com.teleo.manager.generic.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PoliceAssuranceRepository extends GenericRepository<PoliceAssuranceRequest, PoliceAssurance> {

    @Query("SELECT DISTINCT p FROM PoliceAssurance p WHERE p.assurance.id = :assuranceId")
    List<PoliceAssurance> findAllWithAssuranceById(@Param("assuranceId") Long assuranceId);

    @Query("SELECT DISTINCT p FROM PoliceAssurance p LEFT JOIN FETCH p.garanties g WHERE g.id = :garantieId")
    List<PoliceAssurance> findWithGarantieById(@Param("garantieId") Long garantieId);

    @Query("SELECT DISTINCT p FROM PoliceAssurance p LEFT JOIN FETCH p.souscriptions s WHERE s.id = :souscriptionId")
    Optional<PoliceAssurance> findWithSouscriptionById(@Param("souscriptionId") Long souscriptionId);

    @Query("SELECT DISTINCT p FROM PoliceAssurance p WHERE p.numeroPolice = :numeroPolice")
    Optional<PoliceAssurance> findByNumeroPolice(@Param("numeroPolice") String numeroPolice);
}

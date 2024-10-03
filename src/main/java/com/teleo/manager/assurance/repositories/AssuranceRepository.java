package com.teleo.manager.assurance.repositories;

import com.teleo.manager.assurance.dto.request.AssuranceRequest;
import com.teleo.manager.assurance.entities.Assurance;
import com.teleo.manager.assurance.enums.InsuranceType;
import com.teleo.manager.generic.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssuranceRepository extends GenericRepository<AssuranceRequest, Assurance> {

    @Query("SELECT DISTINCT a FROM Assurance a WHERE a.type = :type")
    Optional<Assurance> findAssurancesByType(@Param("type") InsuranceType type);

    @Query("SELECT DISTINCT a FROM Assurance a LEFT JOIN FETCH a.polices p WHERE p.id = :policeId")
    Optional<Assurance> findAssuranceWithPolicesById(@Param("policeId") Long policeId);
}


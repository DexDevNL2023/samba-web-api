package com.teleo.manager.assurance.repositories;

import com.teleo.manager.assurance.dto.request.ContratAssuranceRequest;
import com.teleo.manager.assurance.entities.ContratAssurance;
import com.teleo.manager.generic.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratAssuranceRepository extends GenericRepository<ContratAssuranceRequest, ContratAssurance> {

    @Query("SELECT DISTINCT c FROM ContratAssurance c LEFT JOIN FETCH c.souscription s WHERE s.id = :souscriptionId")
    List<ContratAssurance> findContratWithSouscriptionById(@Param("souscriptionId") Long souscriptionId);
}

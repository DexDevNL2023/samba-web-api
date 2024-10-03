package com.teleo.manager.assurance.repositories;

import com.teleo.manager.assurance.dto.request.GarantieRequest;
import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.assurance.enums.GarantieStatus;
import com.teleo.manager.generic.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarantieRepository extends GenericRepository<GarantieRequest, Garantie> {

    @Query("SELECT DISTINCT g FROM Garantie g WHERE g.status = :status")
    List<Garantie> findGarantiesByStatus(@Param("status") GarantieStatus status);

    @Query("SELECT DISTINCT g FROM Garantie g LEFT JOIN FETCH g.polices p WHERE p.id = :policeId")
    List<Garantie> findGarantieWithPolicesById(@Param("policeId") Long policeId);
}

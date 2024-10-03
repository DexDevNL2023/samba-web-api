package com.teleo.manager.prestation.repositories;

import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.prestation.dto.request.DossierMedicalRequest;
import com.teleo.manager.prestation.entities.DossierMedical;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DossierMedicalRepository extends GenericRepository<DossierMedicalRequest, DossierMedical> {

    @Query("SELECT DISTINCT d FROM DossierMedical d LEFT JOIN FETCH d.patient p WHERE p.id = :patientId")
    List<DossierMedical> findAllWithPatientById(@Param("patientId") Long patientId);

    @Query("SELECT DISTINCT d FROM DossierMedical d WHERE d.dateUpdated BETWEEN :startDate AND :endDate")
    List<DossierMedical> findAllByDateUpdatedBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

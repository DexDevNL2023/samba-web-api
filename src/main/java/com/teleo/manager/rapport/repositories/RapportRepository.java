package com.teleo.manager.rapport.repositories;

import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.rapport.dto.request.RapportRequest;
import com.teleo.manager.rapport.entities.Rapport;
import com.teleo.manager.rapport.enums.RapportType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RapportRepository extends GenericRepository<RapportRequest, Rapport> {

    @Query("SELECT DISTINCT r FROM Rapport r WHERE r.type = :type")
    List<Rapport> findByType(@Param("type") RapportType type);

    @Query("SELECT DISTINCT r FROM Rapport r WHERE r.dateGeneration BETWEEN :startDate AND :endDate")
    List<Rapport> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

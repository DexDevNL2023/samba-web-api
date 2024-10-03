package com.teleo.manager.prestation.repositories;

import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.prestation.dto.request.FinanceurRequest;
import com.teleo.manager.prestation.entities.Financeur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceurRepository extends GenericRepository<FinanceurRequest, Financeur> {
    @Query("SELECT DISTINCT f FROM Financeur f LEFT JOIN FETCH f.prestations p WHERE p.id = :prestationId")
    List<Financeur> findFinanceurWithPrestationsById(@Param("prestationId") Long prestationId);
}

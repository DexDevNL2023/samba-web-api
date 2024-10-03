package com.teleo.manager.parametre.repositories;

import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.parametre.dto.request.BrancheRequest;
import com.teleo.manager.parametre.entities.Branche;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BrancheRepository extends GenericRepository<BrancheRequest, Branche> {

    @Query("SELECT DISTINCT b FROM Branche b LEFT JOIN FETCH b.registrants r WHERE r.id = :registrantId")
    Optional<Branche> findBrancheWithRegistrantsById(@Param("registrantId") Long registrantId);
}


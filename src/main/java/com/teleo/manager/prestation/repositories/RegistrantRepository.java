package com.teleo.manager.prestation.repositories;

import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.prestation.dto.request.RegistrantRequest;
import com.teleo.manager.prestation.entities.Registrant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrantRepository extends GenericRepository<RegistrantRequest, Registrant> {

    @Query("SELECT DISTINCT r FROM Registrant r LEFT JOIN FETCH r.branche b WHERE b.id = :brancheId")
    List<Registrant> findAllRegistrantWithBrancheById(@Param("brancheId") Long brancheId);

    @Query("SELECT DISTINCT r FROM Registrant r LEFT JOIN FETCH r.fournisseur f WHERE f.id = :fournisseurId")
    List<Registrant> findAllRegistrantWithFournisseurById(@Param("fournisseurId") Long fournisseurId);

    @Query("SELECT DISTINCT r FROM Registrant r LEFT JOIN FETCH r.branche b LEFT JOIN FETCH r.fournisseur f WHERE b.id = :brancheId AND f.id = :fournisseurId")
    Optional<Registrant> findRegistrantByBrancheIdAndFournisseurId(@Param("brancheId") Long brancheId, @Param("fournisseurId") Long fournisseurId);
}

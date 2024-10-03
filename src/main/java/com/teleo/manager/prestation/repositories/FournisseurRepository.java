package com.teleo.manager.prestation.repositories;

import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.prestation.entities.Fournisseur;
import com.teleo.manager.prestation.dto.request.FournisseurRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FournisseurRepository extends GenericRepository<FournisseurRequest, Fournisseur> {

    @Query("SELECT DISTINCT f FROM Fournisseur f LEFT JOIN FETCH f.prestations p WHERE p.id = :prestationId")
    Optional<Fournisseur> findFournisseurWithPrestationsById(@Param("prestationId") Long prestationId);

    @Query("SELECT DISTINCT f FROM Fournisseur f LEFT JOIN FETCH f.registrants r WHERE r.id = :registrantId")
    Optional<Fournisseur> findFournisseurWithRegistrantsById(@Param("registrantId") Long registrantId);

    @Query("SELECT DISTINCT f FROM Fournisseur f LEFT JOIN FETCH f.registrants r WHERE r.branche.id = :branchId")
    List<Fournisseur> findFournisseurWithBranchesById(@Param("branchId") Long branchId);

    // Méthode ajoutée
    @Query("SELECT DISTINCT f FROM Fournisseur f WHERE f.account.id = :accountId")
    Optional<Fournisseur> findByAccountId(@Param("accountId") Long accountId);
}

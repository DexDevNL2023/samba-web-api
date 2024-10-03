package com.teleo.manager.prestation.repositories.impl;

import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import com.teleo.manager.prestation.dto.request.FournisseurRequest;
import com.teleo.manager.prestation.entities.Fournisseur;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class FournisseurRepositoryImpl extends GenericRepositoryImpl<FournisseurRequest, Fournisseur> {
    public FournisseurRepositoryImpl(EntityManager entityManager) {
        super(Fournisseur.class, entityManager);
    }
}

package com.teleo.manager.paiement.repositories.impl;

import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import com.teleo.manager.paiement.dto.request.PaiementRequest;
import com.teleo.manager.paiement.entities.Paiement;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PaiementRepositoryImpl extends GenericRepositoryImpl<PaiementRequest, Paiement> {

    public PaiementRepositoryImpl(EntityManager entityManager) {
        super(Paiement.class, entityManager);
    }
}

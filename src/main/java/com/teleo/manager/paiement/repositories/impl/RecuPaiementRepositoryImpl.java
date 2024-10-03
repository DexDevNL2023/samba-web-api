package com.teleo.manager.paiement.repositories.impl;

import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import com.teleo.manager.paiement.dto.request.RecuPaiementRequest;
import com.teleo.manager.paiement.entities.RecuPaiement;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class RecuPaiementRepositoryImpl extends GenericRepositoryImpl<RecuPaiementRequest, RecuPaiement> {
    public RecuPaiementRepositoryImpl(EntityManager entityManager) {
        super(RecuPaiement.class, entityManager);
    }
}

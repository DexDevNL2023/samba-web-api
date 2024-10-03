package com.teleo.manager.prestation.repositories.impl;

import com.teleo.manager.prestation.dto.request.FinanceurRequest;
import com.teleo.manager.prestation.entities.Financeur;
import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class FinanceurRepositoryImpl extends GenericRepositoryImpl<FinanceurRequest, Financeur> {
    public FinanceurRepositoryImpl(EntityManager entityManager) {
        super(Financeur.class, entityManager);
    }
}

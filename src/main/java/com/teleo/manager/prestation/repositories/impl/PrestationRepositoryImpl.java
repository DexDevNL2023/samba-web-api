package com.teleo.manager.prestation.repositories.impl;

import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import com.teleo.manager.prestation.dto.request.PrestationRequest;
import com.teleo.manager.prestation.entities.Prestation;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PrestationRepositoryImpl extends GenericRepositoryImpl<PrestationRequest, Prestation> {
    public PrestationRepositoryImpl(EntityManager entityManager) {
        super(Prestation.class, entityManager);
    }
}

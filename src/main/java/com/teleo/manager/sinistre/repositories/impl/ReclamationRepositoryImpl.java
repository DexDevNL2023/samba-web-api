package com.teleo.manager.sinistre.repositories.impl;

import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import com.teleo.manager.sinistre.dto.request.ReclamationRequest;
import com.teleo.manager.sinistre.entities.Reclamation;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ReclamationRepositoryImpl extends GenericRepositoryImpl<ReclamationRequest, Reclamation> {
    public ReclamationRepositoryImpl(EntityManager entityManager) {
        super(Reclamation.class, entityManager);
    }
}

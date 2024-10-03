package com.teleo.manager.rapport.repositories.impl;

import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import com.teleo.manager.rapport.dto.request.RapportRequest;
import com.teleo.manager.rapport.entities.Rapport;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class RapportRepositoryImpl extends GenericRepositoryImpl<RapportRequest, Rapport> {

    public RapportRepositoryImpl(EntityManager entityManager) {
        super(Rapport.class, entityManager);
    }
}

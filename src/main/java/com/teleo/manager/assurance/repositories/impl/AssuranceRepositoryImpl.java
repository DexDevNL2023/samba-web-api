package com.teleo.manager.assurance.repositories.impl;

import com.teleo.manager.assurance.dto.request.AssuranceRequest;
import com.teleo.manager.assurance.entities.Assurance;
import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class AssuranceRepositoryImpl extends GenericRepositoryImpl<AssuranceRequest, Assurance> {
    public AssuranceRepositoryImpl(EntityManager entityManager) {
        super(Assurance.class, entityManager);
    }
}

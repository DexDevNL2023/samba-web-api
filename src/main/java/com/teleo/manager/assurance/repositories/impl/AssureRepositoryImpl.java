package com.teleo.manager.assurance.repositories.impl;

import com.teleo.manager.assurance.dto.request.AssureRequest;
import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class AssureRepositoryImpl extends GenericRepositoryImpl<AssureRequest, Assure> {
    public AssureRepositoryImpl(EntityManager entityManager) {
        super(Assure.class, entityManager);
    }
}


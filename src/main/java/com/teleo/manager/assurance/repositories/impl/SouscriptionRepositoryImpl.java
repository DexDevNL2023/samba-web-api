package com.teleo.manager.assurance.repositories.impl;

import com.teleo.manager.assurance.dto.request.SouscriptionRequest;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class SouscriptionRepositoryImpl extends GenericRepositoryImpl<SouscriptionRequest, Souscription> {
    public SouscriptionRepositoryImpl(EntityManager entityManager) {
        super(Souscription.class, entityManager);
    }
}

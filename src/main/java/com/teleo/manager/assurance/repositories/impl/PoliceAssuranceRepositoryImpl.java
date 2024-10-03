package com.teleo.manager.assurance.repositories.impl;

import com.teleo.manager.assurance.dto.request.PoliceAssuranceRequest;
import com.teleo.manager.assurance.entities.PoliceAssurance;
import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PoliceAssuranceRepositoryImpl extends GenericRepositoryImpl<PoliceAssuranceRequest, PoliceAssurance> {
    public PoliceAssuranceRepositoryImpl(EntityManager entityManager) {
        super(PoliceAssurance.class, entityManager);
    }
}

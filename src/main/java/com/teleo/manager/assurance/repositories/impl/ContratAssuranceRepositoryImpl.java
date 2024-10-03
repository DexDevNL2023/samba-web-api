package com.teleo.manager.assurance.repositories.impl;

import com.teleo.manager.assurance.dto.request.ContratAssuranceRequest;
import com.teleo.manager.assurance.entities.ContratAssurance;
import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ContratAssuranceRepositoryImpl extends GenericRepositoryImpl<ContratAssuranceRequest, ContratAssurance> {
    public ContratAssuranceRepositoryImpl(EntityManager entityManager) {
        super(ContratAssurance.class, entityManager);
    }
}

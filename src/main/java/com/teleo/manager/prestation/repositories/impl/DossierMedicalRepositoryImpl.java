package com.teleo.manager.prestation.repositories.impl;

import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import com.teleo.manager.prestation.dto.request.DossierMedicalRequest;
import com.teleo.manager.prestation.entities.DossierMedical;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class DossierMedicalRepositoryImpl extends GenericRepositoryImpl<DossierMedicalRequest, DossierMedical> {
    public DossierMedicalRepositoryImpl(EntityManager entityManager) {
        super(DossierMedical.class, entityManager);
    }
}


package com.teleo.manager.assurance.repositories.impl;

import com.teleo.manager.assurance.dto.request.GarantieRequest;
import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class GarantieRepositoryImpl extends GenericRepositoryImpl<GarantieRequest, Garantie> {
    public GarantieRepositoryImpl(EntityManager entityManager) {
        super(Garantie.class, entityManager);
    }
}

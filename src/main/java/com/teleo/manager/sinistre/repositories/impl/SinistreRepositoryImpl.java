package com.teleo.manager.sinistre.repositories.impl;

import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import com.teleo.manager.sinistre.dto.request.SinistreRequest;
import com.teleo.manager.sinistre.entities.Sinistre;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class SinistreRepositoryImpl extends GenericRepositoryImpl<SinistreRequest, Sinistre> {
    public SinistreRepositoryImpl(EntityManager entityManager) {
        super(Sinistre.class, entityManager);
    }
}

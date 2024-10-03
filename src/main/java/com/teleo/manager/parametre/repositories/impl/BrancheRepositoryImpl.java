package com.teleo.manager.parametre.repositories.impl;

import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import com.teleo.manager.parametre.dto.request.BrancheRequest;
import com.teleo.manager.parametre.entities.Branche;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class BrancheRepositoryImpl extends GenericRepositoryImpl<BrancheRequest, Branche> {

    public BrancheRepositoryImpl(EntityManager entityManager) {
        super(Branche.class, entityManager);
    }
}

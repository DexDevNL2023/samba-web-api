package com.teleo.manager.prestation.repositories.impl;

import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import com.teleo.manager.prestation.dto.request.RegistrantRequest;
import com.teleo.manager.prestation.entities.Registrant;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class RegistrantRepositoryImpl extends GenericRepositoryImpl<RegistrantRequest, Registrant> {

    public RegistrantRepositoryImpl(EntityManager entityManager) {
        super(Registrant.class, entityManager);
    }
}

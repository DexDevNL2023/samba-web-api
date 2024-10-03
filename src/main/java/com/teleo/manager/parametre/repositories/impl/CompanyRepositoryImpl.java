package com.teleo.manager.parametre.repositories.impl;

import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import com.teleo.manager.parametre.dto.request.CompanyRequest;
import com.teleo.manager.parametre.entities.Company;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyRepositoryImpl extends GenericRepositoryImpl<CompanyRequest, Company> {

    public CompanyRepositoryImpl(EntityManager entityManager) {
        super(Company.class, entityManager);
    }
}

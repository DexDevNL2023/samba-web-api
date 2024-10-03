package com.teleo.manager.authentification.repositories.impl;

import com.teleo.manager.authentification.dto.request.RoleRequest;
import com.teleo.manager.authentification.entities.Role;
import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<RoleRequest, Role> {
    public RoleRepositoryImpl(EntityManager entityManager) {
        super(Role.class, entityManager);
    }
}

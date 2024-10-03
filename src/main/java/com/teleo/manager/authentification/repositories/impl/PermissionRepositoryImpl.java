package com.teleo.manager.authentification.repositories.impl;

import com.teleo.manager.authentification.dto.request.PermissionRequest;
import com.teleo.manager.authentification.entities.Permission;
import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PermissionRepositoryImpl extends GenericRepositoryImpl<PermissionRequest, Permission> {
    public PermissionRepositoryImpl(EntityManager entityManager) {
        super(Permission.class, entityManager);
    }
}

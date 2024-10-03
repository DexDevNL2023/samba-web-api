package com.teleo.manager.authentification.services.impl;

import com.teleo.manager.authentification.dto.reponse.PermissionResponse;
import com.teleo.manager.authentification.dto.request.PermissionRequest;
import com.teleo.manager.authentification.entities.Permission;
import com.teleo.manager.authentification.mapper.PermissionMapper;
import com.teleo.manager.authentification.repositories.PermissionRepository;
import com.teleo.manager.authentification.services.PermissionService;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PermissionServiceImpl extends ServiceGenericImpl<PermissionRequest, PermissionResponse, Permission> implements PermissionService {
    
    private final PermissionRepository repository;

    public PermissionServiceImpl(PermissionRepository repository, PermissionMapper mapper) {
        super(Permission.class, repository, mapper);
        this.repository = repository;
    }

    @Transactional
    @LogExecution
    @Override
    public Permission findByPermissionKey(String permissionKey) {
        return repository.findByPermissionKey(permissionKey);
    }
}

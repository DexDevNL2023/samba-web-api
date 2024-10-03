package com.teleo.manager.authentification.services.impl;

import com.teleo.manager.authentification.dto.reponse.RoleResponse;
import com.teleo.manager.authentification.dto.request.RoleRequest;
import com.teleo.manager.authentification.entities.Role;
import com.teleo.manager.authentification.mapper.RoleMapper;
import com.teleo.manager.authentification.repositories.RoleRepository;
import com.teleo.manager.authentification.services.RoleService;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl extends ServiceGenericImpl<RoleRequest, RoleResponse, Role> implements RoleService {

    private  final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository, RoleMapper mapper) {
        super(Role.class, repository, mapper);
        this.repository = repository;
    }

    @Transactional
    @LogExecution
    @Override
    public Role findByRoleKey(String roleKey) {
        return repository.findByRoleKey(roleKey);
    }

    @Override
    public List<RoleResponse> findAllByAccountId(Long accountId) {
        return mapper.toDto(repository.findAllByAccountId(accountId));
    }
}

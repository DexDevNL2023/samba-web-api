package com.teleo.manager.authentification.services;

import com.teleo.manager.authentification.dto.reponse.RoleResponse;
import com.teleo.manager.authentification.dto.request.RoleRequest;
import com.teleo.manager.authentification.entities.Role;
import com.teleo.manager.generic.service.ServiceGeneric;

import java.util.List;

public interface RoleService extends ServiceGeneric<RoleRequest, RoleResponse, Role> {
    Role findByRoleKey(String roleKey);
    List<RoleResponse> findAllByAccountId(Long accountId);
}

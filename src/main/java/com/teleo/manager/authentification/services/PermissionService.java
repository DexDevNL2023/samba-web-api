package com.teleo.manager.authentification.services;

import com.teleo.manager.authentification.dto.reponse.PermissionResponse;
import com.teleo.manager.authentification.dto.request.PermissionRequest;
import com.teleo.manager.authentification.entities.Permission;
import com.teleo.manager.generic.service.ServiceGeneric;

public interface PermissionService extends ServiceGeneric<PermissionRequest, PermissionResponse, Permission> {
    Permission findByPermissionKey(String permissionKey);
}

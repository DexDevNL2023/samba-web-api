package com.teleo.manager.authentification.mapper;

import com.teleo.manager.authentification.dto.reponse.PermissionResponse;
import com.teleo.manager.authentification.dto.request.PermissionRequest;
import com.teleo.manager.authentification.entities.Permission;
import com.teleo.manager.generic.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends GenericMapper<PermissionRequest, PermissionResponse, Permission> {
}

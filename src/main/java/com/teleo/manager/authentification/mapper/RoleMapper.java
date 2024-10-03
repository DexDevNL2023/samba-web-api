package com.teleo.manager.authentification.mapper;

import com.teleo.manager.authentification.dto.reponse.RoleFormResponse;
import org.mapstruct.*;

import com.teleo.manager.authentification.dto.reponse.RoleResponse;
import com.teleo.manager.authentification.dto.request.RoleRequest;
import com.teleo.manager.authentification.entities.Role;
import com.teleo.manager.authentification.services.PermissionService;
import com.teleo.manager.generic.mapper.GenericMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PermissionService.class})
public interface RoleMapper extends GenericMapper<RoleRequest, RoleResponse, Role> {

    @Mapping(target = "account", ignore = true)
    Role toEntity(RoleRequest dto);

    @Mapping(target = "account", ignore = true)
    RoleResponse toDto(Role entity);

    @Mapping(target = "account", ignore = true)
    List<RoleFormResponse> mapAll(List<Role> roles);
}
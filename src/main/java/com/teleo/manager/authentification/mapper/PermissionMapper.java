package com.teleo.manager.authentification.mapper;

import com.teleo.manager.authentification.dto.reponse.PermissionResponse;
import org.mapstruct.Mapper;

import com.teleo.manager.authentification.dto.request.PermissionRequest;
import com.teleo.manager.authentification.entities.Permission;
import com.teleo.manager.generic.mapper.GenericMapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends GenericMapper<PermissionRequest, PermissionResponse, Permission> {
/*
    @Mapping(target = "roles", ignore = true)
    Permission toEntity(PermissionRequest dto);

    @Mapping(target = "roles", ignore = true)
    PermissionResponse toDto(Permission entity);*/
}

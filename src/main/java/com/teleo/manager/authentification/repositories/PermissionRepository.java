package com.teleo.manager.authentification.repositories;

import com.teleo.manager.authentification.dto.request.PermissionRequest;
import com.teleo.manager.authentification.entities.Permission;
import com.teleo.manager.generic.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends GenericRepository<PermissionRequest, Permission> {
    @Query("SELECT DISTINCT e FROM Permission e WHERE e.permissionKey = :permissionKey")
    Permission findByPermissionKey(@Param("permissionKey") String permissionKey);
}

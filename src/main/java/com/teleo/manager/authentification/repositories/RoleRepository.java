package com.teleo.manager.authentification.repositories;

import com.teleo.manager.authentification.dto.request.RoleRequest;
import com.teleo.manager.authentification.entities.Role;
import com.teleo.manager.generic.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends GenericRepository<RoleRequest, Role> {
  @Query("SELECT DISTINCT r FROM Role r WHERE r.roleKey = :roleKey")
  Role findByRoleKey(@Param("roleKey") String roleKey);

  @Query("SELECT DISTINCT r FROM Role r LEFT JOIN FETCH r.account a WHERE a.id = :accountId")
  List<Role> findAllByAccountId(@Param("accountId") Long accountId);
}

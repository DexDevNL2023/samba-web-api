package com.teleo.manager.authentification.repositories;

import com.teleo.manager.authentification.dto.request.AccountRequest;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.authentification.enums.Authority;
import com.teleo.manager.generic.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends GenericRepository<AccountRequest, Account> {
    @Query("SELECT DISTINCT u FROM Account u WHERE u.email = :email")
    Optional<Account> findByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT u FROM Account u WHERE u.login = :login")
    Optional<Account> findByLogin(@Param("login") String login);

    @Query("SELECT DISTINCT u FROM Account u WHERE u.resetPasswordToken = :token")
    Optional<Account> findByResetPasswordToken(@Param("token") String token);

    @Query("SELECT CASE WHEN COUNT(u.id) > 0 THEN TRUE ELSE FALSE END FROM Account u WHERE u.email = :email")
    Boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u.id) > 0 THEN TRUE ELSE FALSE END FROM Account u WHERE u.login = :login")
    Boolean existsByLogin(@Param("login") String login);

    @Query("SELECT DISTINCT u FROM Account u WHERE u.authority = :name")
    List<Account> findAllByAuthority(@Param("name") Authority name);

    @Query("SELECT DISTINCT a FROM Account a LEFT JOIN FETCH a.roles r WHERE r.id = :roleId")
    List<Account> findUserWithRolesById(@Param("roleId") Long roleId);
}

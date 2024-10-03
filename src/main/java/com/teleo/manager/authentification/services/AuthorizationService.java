package com.teleo.manager.authentification.services;

import com.teleo.manager.authentification.dto.reponse.AccountResponse;
import com.teleo.manager.authentification.mapper.AccountMapper;
import com.teleo.manager.authentification.mapper.RoleMapper;
import jakarta.transaction.Transactional;

import com.teleo.manager.authentification.dto.reponse.RoleFormResponse;
import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.dto.request.PermissionFormRequest;
import com.teleo.manager.authentification.entities.*;
import com.teleo.manager.authentification.enums.Authority;
import com.teleo.manager.authentification.repositories.AccountRepository;
import com.teleo.manager.authentification.repositories.PermissionRepository;
import com.teleo.manager.authentification.repositories.RoleRepository;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthorizationService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;
    private final AccountMapper accountMapper;

    public AuthorizationService(AccountRepository accountRepository, RoleRepository roleRepository, RoleMapper roleMapper, PermissionRepository permissionRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.permissionRepository = permissionRepository;
        this.accountMapper = accountMapper;
    }

    @Transactional
    @LogExecution
    public AccountResponse changeAutorisation(PermissionFormRequest dto) {
        // Récupérer l'utilisateur par ID
        Account account = accountRepository.findById(dto.getAccountId())
            .orElseThrow(() -> new RessourceNotFoundException("L'utilisateur avec l'id " + dto.getAccountId() + " n'existe pas!"));
    
        // Récupérer le module par ID
        Role role = roleRepository.findById(dto.getModuleId())
            .orElseThrow(() -> new RessourceNotFoundException("Le module avec l'id " + dto.getModuleId() + " n'existe pas!"));
    
        // Récupérer les permissions par leurs IDs
        List<Permission> permissions = permissionRepository.findAllById(dto.getPermissionIds());
        if (permissions.size() != dto.getPermissionIds().size()) {
            throw new RessourceNotFoundException("Certaines permissions n'ont pas été trouvées!");
        }
    
        // Assigner les permissions au module
        role.setPermissions(permissions);
    
        // Ajouter le module à l'utilisateur
        account.addRoles(role);
        account = accountRepository.save(account);

        // Mapper Dto
        return accountMapper.toDto(account);
    }

    @Transactional
    @LogExecution
    public List<RoleFormResponse> getAutorisations(Long userId) {
        // Récupérer l'utilisateur par ID
        Account account = accountRepository.findById(userId)
            .orElseThrow(() -> new RessourceNotFoundException("L'utilisateur avec l'id " + userId + " n'existe pas!"));
        List<Role> roles = account.getRoles();
        // Mapper Dto
        return roleMapper.mapAll(roles);
    }

    @Transactional
    @LogExecution
    public void checkIfHasDroit(DroitAddRequest dto) {
        for (Role rule : getCurrentUser().getRoles()) {
            if (rule.getRoleKey().equals(dto.getRoleKey())) {
                for (Permission permission : rule.getPermissions()) {
                    if (permission.getPermissionKey().equals(dto.getPermissionKey())) return;
                }
            }
        }
        throw new RessourceNotFoundException("Vous n'etes pas autoriser a " + dto.getPermissionKey() + " dans " + dto.getRoleKey());
    }

    @Transactional
    @LogExecution
    public Account getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new RessourceNotFoundException("Impossible de retouver l'utilisateur courant!");
        }
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return accountRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RessourceNotFoundException("Aucun utilisateur n'existe avec le nom utilisateur " + userPrincipal.getUsername()));
    }

    @Transactional
    @LogExecution
    public Boolean isAdmin() {
        return getCurrentUser().getAuthority().equals(Authority.ADMIN);
    }
}

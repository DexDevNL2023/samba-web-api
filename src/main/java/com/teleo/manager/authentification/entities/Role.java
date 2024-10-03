package com.teleo.manager.authentification.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.teleo.manager.authentification.dto.request.RoleRequest;
import com.teleo.manager.generic.entity.audit.BaseEntity;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles_utilisateurs")
public class Role extends BaseEntity<Role, RoleRequest> {

    private static final String ENTITY_NAME = "ROLE";

    private static final String MODULE_NAME = "ACCOUNT_MODULE";

    @Column(nullable = false)
    private String roleKey;

    private String libelle;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions = new ArrayList<>();

    // Parameterized constructor
    public Role(String roleKey, String libelle, List<Permission> permissions) {
        this.roleKey = roleKey;
        this.libelle = libelle;
        this.permissions = permissions;
    }

    @Override
    public void update(Role source) {
        this.roleKey = source.getRoleKey();
        this.libelle = source.getLibelle();

        // Mise à jour de l'entité Account associée
        this.account = source.getAccount();

        // Mise à jour de la liste des permissions
        this.permissions.clear();
        if (source.getPermissions() != null) {
            this.permissions.addAll(source.getPermissions());
        }
    }

    @Override
    public boolean equalsToDto(RoleRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = roleKey.equals(source.getRoleKey()) &&
                libelle.equals(source.getLibelle());

        // Comparaison de l'entité Account
        boolean isAccountEqual = (account == null && source.getAccount() == null) ||
                (account != null && account.getId().equals(source.getAccount()));

        // Comparaison des listes de permissions (basée sur la taille)
        boolean arePermissionsEqual = (source.getPermissions() == null && permissions.isEmpty()) ||
                (source.getPermissions() != null && source.getPermissions().size() == permissions.size());

        return areFieldsEqual && isAccountEqual && arePermissionsEqual;
    }

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public String toString() {
        return "Rôle{" +
                "id=" + getId() +
                ", clé='" + roleKey + '\'' +
                ", libellé='" + libelle + '\'' +
                ", permissions=" + permissions.stream()
                .map(permission -> "Permission{clé='" + permission.getPermissionKey() + "'}")
                .collect(Collectors.joining(", ")) +
                ", compteId=" + (account != null ? account.getId() : "Aucun") +
                '}';
    }
}

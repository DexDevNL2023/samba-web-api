package com.teleo.manager.authentification.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.teleo.manager.authentification.dto.request.PermissionRequest;
import com.teleo.manager.generic.entity.audit.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permissions_utilisateurs")
public class Permission extends BaseEntity<Permission, PermissionRequest> {

    private static final String ENTITY_NAME = "PERMISSION";

    private static final String MODULE_NAME = "ACCOUNT_MODULE";

    @Column(nullable = false, unique = true)
    private String permissionKey;

    private String libelle;

    // Parameterized constructor
    public Permission(Long id, String permissionKey, String libelle) {
        this.id = id;
        this.permissionKey = permissionKey;
        this.libelle = libelle;
    }

    @Override
    public void update(Permission source) {
        this.permissionKey = source.getPermissionKey();
        this.libelle = source.getLibelle();

        /*// Mise à jour de la liste des rôles
        this.roles.clear();
        if (source.getRoles() != null) {
            this.roles.addAll(source.getRoles());
        }*/
    }

    @Override
    public boolean equalsToDto(PermissionRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = permissionKey.equals(source.getPermissionKey()) &&
                libelle.equals(source.getLibelle());

        /*// Comparaison des listes de rôles (basée sur la taille)
        boolean areRolesEqual = (source.getRoles() == null && roles.isEmpty()) ||
                (source.getRoles() != null && source.getRoles().size() == roles.size());*/

        return areFieldsEqual; //&& areRolesEqual;
    }

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}

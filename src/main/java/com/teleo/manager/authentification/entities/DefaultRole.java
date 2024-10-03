package com.teleo.manager.authentification.entities;

import lombok.Data;

@Data
public class DefaultRole {
    private final String roleKey;
    private final String libelle;
    private final Long[] permissionIds;
}
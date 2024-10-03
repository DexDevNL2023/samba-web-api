package com.teleo.manager.authentification.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.teleo.manager.authentification.services.RoleService;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.unique.UniqueValidator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleRequest extends BaseRequest {
    @UniqueValidator(service = RoleService.class, fieldName = "roleKey", message = "La clé du module {} est déjà utilisé")
    private String roleKey;
    private String libelle;
    private Long account;
    private List<Long> permissions = new ArrayList<>();
} 

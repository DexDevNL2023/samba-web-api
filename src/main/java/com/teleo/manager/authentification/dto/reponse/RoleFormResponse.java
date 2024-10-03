package com.teleo.manager.authentification.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleFormResponse {
    private String roleKey;
    private String libelle;
    private List<PermissionResponse> permissions = new ArrayList<>();
}

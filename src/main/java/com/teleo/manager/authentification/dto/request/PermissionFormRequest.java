package com.teleo.manager.authentification.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PermissionFormRequest {
    private Long accountId;
    private Long moduleId;
	private List<Long> permissionIds = new ArrayList<>();
}
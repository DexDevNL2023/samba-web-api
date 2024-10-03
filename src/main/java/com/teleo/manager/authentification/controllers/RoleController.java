package com.teleo.manager.authentification.controllers;

import com.teleo.manager.authentification.dto.reponse.RoleResponse;
import com.teleo.manager.authentification.dto.request.RoleRequest;
import com.teleo.manager.authentification.entities.Role;
import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface RoleController extends ControllerGeneric<RoleRequest, RoleResponse, Role> {
    ResponseEntity<RessourceResponse<List<RoleResponse>>> getAllByAccountId(@NotNull @PathVariable("accountId") Long accountId);
}

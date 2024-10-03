package com.teleo.manager.authentification.controllers.impl;

import com.teleo.manager.authentification.dto.reponse.RoleResponse;
import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.teleo.manager.authentification.controllers.RoleController;
import com.teleo.manager.authentification.dto.request.RoleRequest;
import com.teleo.manager.authentification.entities.Role;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.authentification.services.RoleService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "API de gestion des rôles")
public class RoleControllerImpl extends ControllerGenericImpl<RoleRequest, RoleResponse, Role> implements RoleController {

    private static final String MODULE_NAME = "ACCOUNT_MODULE";

    private final RoleService service;
    private final AuthorizationService authorizationService;

    protected RoleControllerImpl(RoleService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Role newInstance() {
        return new Role();
    }

    @GetMapping(value = "/find/by/account/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<RoleResponse>>> getAllByAccountId(@NotNull @PathVariable("accountId") Long accountId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Roles récupérés avec succès!", service.findAllByAccountId(accountId)), HttpStatus.OK);
    }
}

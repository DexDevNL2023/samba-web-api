package com.teleo.manager.parametre.controllers.impl;

import com.teleo.manager.parametre.controllers.BrancheController;
import com.teleo.manager.parametre.dto.reponse.BrancheResponse;
import com.teleo.manager.parametre.dto.request.BrancheRequest;
import com.teleo.manager.parametre.entities.Branche;
import com.teleo.manager.parametre.services.BrancheService;
import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RefreshScope
@RestController
@RequestMapping("/api/branches")
@Tag(name = "Branches", description = "API de gestion des branches")
public class BrancheControllerImpl extends ControllerGenericImpl<BrancheRequest, BrancheResponse, Branche> implements BrancheController {

    private static final String MODULE_NAME = "BRANCHE_MODULE";

    private final BrancheService service;
    private final AuthorizationService authorizationService;

    public BrancheControllerImpl(BrancheService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Branche newInstance() {
        return new Branche();
    }

    @GetMapping(value = "/find/by/registrant/{registrantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<BrancheResponse>> getBrancheWithRegistrantsById(@NotNull @PathVariable("registrantId") Long registrantId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<BrancheResponse>("Branche retrouvée avec succès!", service.findBrancheWithRegistrantsById(registrantId)), HttpStatus.OK);
    }
}

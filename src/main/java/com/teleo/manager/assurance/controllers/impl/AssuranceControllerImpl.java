package com.teleo.manager.assurance.controllers.impl;

import com.teleo.manager.assurance.controllers.AssuranceController;
import com.teleo.manager.assurance.dto.reponse.AssuranceResponse;
import com.teleo.manager.assurance.dto.request.AssuranceRequest;
import com.teleo.manager.assurance.entities.Assurance;
import com.teleo.manager.assurance.enums.InsuranceType;
import com.teleo.manager.assurance.services.AssuranceService;
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

import java.util.List;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/assurances")
@Tag(name = "Assurances", description = "API de gestion des assurances")
public class AssuranceControllerImpl extends ControllerGenericImpl<AssuranceRequest, AssuranceResponse, Assurance> implements AssuranceController {

    private static final String MODULE_NAME = "ASSURANCE_MODULE";

    private final AssuranceService service;
    private final AuthorizationService authorizationService;

    protected AssuranceControllerImpl(AssuranceService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Assurance newInstance() {
        return new Assurance();
    }

    @GetMapping(value = "/find/by/police/{policeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<AssuranceResponse>> getAssuranceWithPolicesById(@NotNull @PathVariable("policeId") Long policeId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Assurance retrouvée avec succès!", service.findAssuranceWithPolicesById(policeId)), HttpStatus.OK);
    }

    @Override
    @GetMapping(value = "/find/by/type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<AssuranceResponse>> getAssurancesByType(@NotNull @PathVariable("type") InsuranceType type) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Assurance retrouvée avec succès!", service.findAssurancesByType(type)), HttpStatus.OK);
    }
}


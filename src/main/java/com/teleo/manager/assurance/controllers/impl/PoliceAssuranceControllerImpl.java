package com.teleo.manager.assurance.controllers.impl;

import com.teleo.manager.assurance.controllers.PoliceAssuranceController;
import com.teleo.manager.assurance.dto.reponse.PoliceAssuranceResponse;
import com.teleo.manager.assurance.dto.request.PoliceAssuranceRequest;
import com.teleo.manager.assurance.entities.PoliceAssurance;
import com.teleo.manager.assurance.services.PoliceAssuranceService;
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
@RequestMapping("/api/polices/assurances")
@Tag(name = "Polices d'assurance", description = "API de gestion des polices d'assurance")
public class PoliceAssuranceControllerImpl extends ControllerGenericImpl<PoliceAssuranceRequest, PoliceAssuranceResponse, PoliceAssurance> implements PoliceAssuranceController {

    private static final String MODULE_NAME = "POLICE_ASSURANCE_MODULE";

    private final PoliceAssuranceService service;
    private final AuthorizationService authorizationService;

    protected PoliceAssuranceControllerImpl(PoliceAssuranceService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected PoliceAssurance newInstance() {
        return new PoliceAssurance();
    }

    @GetMapping(value = "/find/by/assurance/{assuranceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<PoliceAssuranceResponse>>> getAllWithAssuranceById(@NotNull @PathVariable("assuranceId") Long assuranceId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Police d'assurance retrouvée avec succès!", service.findAllWithAssuranceById(assuranceId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/garantie/{garantieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<PoliceAssuranceResponse>>> getWithGarantiesById(@NotNull @PathVariable("garantieId") Long garantieId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Police d'assurance retrouvée avec succès!", service.findWithGarantieById(garantieId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/souscription/{souscriptionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<PoliceAssuranceResponse>> getdWithSouscriptionsById(@NotNull @PathVariable("souscriptionId") Long souscriptionId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Police d'assurance retrouvée avec succès!", service.findWithSouscriptionById(souscriptionId)), HttpStatus.OK);
    }
}

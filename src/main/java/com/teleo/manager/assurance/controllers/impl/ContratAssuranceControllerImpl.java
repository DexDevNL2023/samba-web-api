package com.teleo.manager.assurance.controllers.impl;

import com.teleo.manager.assurance.controllers.ContratAssuranceController;
import com.teleo.manager.assurance.dto.reponse.ContratAssuranceResponse;
import com.teleo.manager.assurance.dto.request.ContratAssuranceRequest;
import com.teleo.manager.assurance.entities.ContratAssurance;
import com.teleo.manager.assurance.services.ContratAssuranceService;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.authentification.dto.request.DroitAddRequest;
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
@RequestMapping("/api/contrats/assurances")
@Tag(name = "Contrats d'assurance", description = "API de gestion des contrats d'assurance")
public class ContratAssuranceControllerImpl extends ControllerGenericImpl<ContratAssuranceRequest, ContratAssuranceResponse, ContratAssurance> implements ContratAssuranceController {

    private static final String MODULE_NAME = "CONTRAT_MODULE";

    private final ContratAssuranceService service;
    private final AuthorizationService authorizationService;

    public ContratAssuranceControllerImpl(ContratAssuranceService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected ContratAssurance newInstance() {
        return new ContratAssurance();
    }

    @GetMapping(value = "/find/by/souscription/{souscriptionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<ContratAssuranceResponse>>> getContratsBySouscriptionId(@NotNull @PathVariable("souscriptionId") Long souscriptionId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Contrats d'assurance trouvés avec succès!", service.findContratWithSouscriptionById(souscriptionId)), HttpStatus.OK);
    }
}

package com.teleo.manager.sinistre.controllers.impl;

import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.sinistre.controllers.ReclamationController;
import com.teleo.manager.sinistre.dto.reponse.ReclamationResponse;
import com.teleo.manager.sinistre.dto.request.ReclamationRequest;
import com.teleo.manager.sinistre.entities.Reclamation;
import com.teleo.manager.sinistre.services.ReclamationService;
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
@RequestMapping("/api/reclamations")
@Tag(name = "Reclamations", description = "API de gestion des reclamations")
public class ReclamationControllerImpl extends ControllerGenericImpl<ReclamationRequest, ReclamationResponse, Reclamation> implements ReclamationController {

    private static final String MODULE_NAME = "RECLAMATION_MODULE";

    private final ReclamationService service;
    private final AuthorizationService authorizationService;

    public ReclamationControllerImpl(ReclamationService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Reclamation newInstance() {
        return new Reclamation();
    }

    @GetMapping(value = "/find/by/souscription/{souscriptionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<ReclamationResponse>>> getBySouscriptionId(
            @NotNull @PathVariable("souscriptionId") Long souscriptionId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Reclamations retrouvés avec succès!", service.findAllBySouscriptionId(souscriptionId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/sinistre/{sinistreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<ReclamationResponse>>> getAllBySinistreId(
            @NotNull @PathVariable("sinistreId") Long sinistreId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Reclamations retrouvés avec succès!", service.findAllBySinistreId(sinistreId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/prestation/{prestationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<ReclamationResponse>>> getAllByPrestationId(
            @NotNull @PathVariable("prestationId") Long prestationId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Reclamations retrouvés avec succès!", service.findAllByPrestationId(prestationId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/paiement/{paiementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<ReclamationResponse>> getWithPaiementsById(@NotNull @PathVariable("paiementId") Long paiementId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Reclamations retrouvés avec succès!", service.findWithPaiementsById(paiementId)), HttpStatus.OK);
    }
}

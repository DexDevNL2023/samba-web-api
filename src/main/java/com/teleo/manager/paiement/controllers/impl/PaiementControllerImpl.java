package com.teleo.manager.paiement.controllers.impl;

import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.paiement.controllers.PaiementController;
import com.teleo.manager.paiement.dto.reponse.PaiementResponse;
import com.teleo.manager.paiement.dto.request.PaiementRequest;
import com.teleo.manager.paiement.entities.Paiement;
import com.teleo.manager.paiement.services.PaiementService;
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
@RequestMapping("/api/paiements")
@Tag(name = "Paiements", description = "API de gestion des paiements")
public class PaiementControllerImpl extends ControllerGenericImpl<PaiementRequest, PaiementResponse, Paiement> implements PaiementController {

    private static final String MODULE_NAME = "PAIEMENT_MODULE";

    private final PaiementService service;
    private final AuthorizationService authorizationService;

    protected PaiementControllerImpl(PaiementService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Paiement newInstance() {
        return new Paiement();
    }

    @GetMapping(value = "/find/by/souscription/{souscriptionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<PaiementResponse>>> getBySouscriptionId(
            @NotNull @PathVariable("souscriptionId") Long souscriptionId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Paiements retrouvés avec succès!", service.findAllBySouscriptionId(souscriptionId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/reclamation/{sinistreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<PaiementResponse>>> getByReclamationId(
            @NotNull @PathVariable("reclamationId") Long reclamationId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Paiement retrouvé avec succès!", service.findByReclamationId(reclamationId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/recu/{recuPaiementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<PaiementResponse>> getByRecuPaiementId(
            @NotNull @PathVariable("recuPaiementId") Long recuPaiementId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Paiement retrouvé avec succès!", service.findByRecuPaiementId(recuPaiementId)), HttpStatus.OK);
    }
}

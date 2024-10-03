package com.teleo.manager.prestation.controllers.impl;

import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.paiement.dto.reponse.PaiementResponse;
import com.teleo.manager.prestation.controllers.PrestationController;
import com.teleo.manager.prestation.dto.reponse.PrestationResponse;
import com.teleo.manager.prestation.dto.request.PrestationRequest;
import com.teleo.manager.prestation.entities.Prestation;
import com.teleo.manager.prestation.services.PrestationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/prestations")
@Tag(name = "Prestations", description = "API de gestion des prestations")
public class PrestationControllerImpl extends ControllerGenericImpl<PrestationRequest, PrestationResponse, Prestation> implements PrestationController {

    private static final String MODULE_NAME = "PRESTATION_MODULE";

    private final PrestationService service;
    private final AuthorizationService authorizationService;

    public PrestationControllerImpl(PrestationService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Prestation newInstance() {
        return new Prestation();
    }

    @GetMapping(value = "/find/by/souscription/{souscriptionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<PrestationResponse>>> getBySouscriptionId(
            @NotNull @PathVariable("souscriptionId") Long souscriptionId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Prestations retrouvés avec succès!", service.findAllBySouscriptionId(souscriptionId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/sinistre/{sinistreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<PrestationResponse>>> getBySinistreId(
            @NotNull @PathVariable("sinistreId") Long sinistreId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Prestations retrouvés avec succès!", service.findAllBySinistreId(sinistreId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/document/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<PrestationResponse>> getWithDocumentsById(
            @NotNull @PathVariable("documentId") Long documentId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Prestations retrouvés avec succès!", service.findWithDocumentsById(documentId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/fournisseur/{fournisseurId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<PrestationResponse>>> getByFournisseurId(
            @NotNull @PathVariable("fournisseurId") Long fournisseurId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Prestations retrouvés avec succès!", service.findAllByFournisseurId(fournisseurId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/financeur/{financeurId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<PrestationResponse>>> getWithFinanceursById(@NotNull @PathVariable("financeurId") Long financeurId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Prestation retrouvé avec succès!", service.findWithFinanceursById(financeurId)), HttpStatus.OK);
    }
}

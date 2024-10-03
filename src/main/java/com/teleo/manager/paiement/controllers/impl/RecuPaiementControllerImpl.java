package com.teleo.manager.paiement.controllers.impl;

import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.paiement.controllers.RecuPaiementController;
import com.teleo.manager.paiement.dto.reponse.RecuPaiementResponse;
import com.teleo.manager.paiement.dto.request.RecuPaiementRequest;
import com.teleo.manager.paiement.entities.RecuPaiement;
import com.teleo.manager.paiement.services.RecuPaiementService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/recus/paiements")
@Tag(name = "Reçus de paiements", description = "API de gestion des reçus de paiements")
public class RecuPaiementControllerImpl extends ControllerGenericImpl<RecuPaiementRequest, RecuPaiementResponse, RecuPaiement> implements RecuPaiementController {

    private static final String MODULE_NAME = "RECU_PAIEMENT_MODULE";

    private final RecuPaiementService service;
    private final AuthorizationService authorizationService;

    protected RecuPaiementControllerImpl(RecuPaiementService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected RecuPaiement newInstance() {
        return new RecuPaiement();
    }

    @GetMapping(value = "/find/by/numero/{numeroRecu}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<RecuPaiementResponse>> getRecuPaiementByNumeroRecu(@NotNull @PathVariable("numeroRecu") String numeroRecu) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Reçu de paiement retrouvée avec succès!", service.findByNumeroRecu(numeroRecu)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/date/range", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<RecuPaiementResponse>>> getRecuPaiementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Reçu de paiement retrouvée avec succès!", service.findAllByDateRange(startDate, endDate)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/montant/range", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<RecuPaiementResponse>>> getRecuPaiementsByMontantRange(
            @RequestParam BigDecimal minMontant,
            @RequestParam BigDecimal maxMontant) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Reçu de paiement retrouvée avec succès!", service.findAllByMontantRange(minMontant, maxMontant)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/paiement/{paiementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<RecuPaiementResponse>>> getByPaiementId(@NotNull @PathVariable("paiementId") Long paiementId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Reçu de paiement retrouvée avec succès!", service.findByPaiementId(paiementId)), HttpStatus.OK);
    }
}

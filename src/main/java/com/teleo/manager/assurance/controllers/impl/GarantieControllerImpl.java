package com.teleo.manager.assurance.controllers.impl;

import com.teleo.manager.assurance.controllers.GarantieController;
import com.teleo.manager.assurance.dto.reponse.GarantieResponse;
import com.teleo.manager.assurance.dto.request.GarantieRequest;
import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.assurance.services.GarantieService;
import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
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
@RequestMapping("/api/garanties")
@Tag(name = "Garanties", description = "API de gestion des garanties")
public class GarantieControllerImpl extends ControllerGenericImpl<GarantieRequest, GarantieResponse, Garantie> implements GarantieController {

    private static final String MODULE_NAME = "GARANTIE_MODULE";

    private final GarantieService service;
    private final AuthorizationService authorizationService;

    public GarantieControllerImpl(GarantieService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Garantie newInstance() {
        return new Garantie();
    }

    @GetMapping(value = "/find/by/police/{policeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<GarantieResponse>>> getGarantieWithPolicesById(@NotNull @PathVariable("policeId") Long policeId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Garantie retrouvée avec succès!", service.findGarantieWithPolicesById(policeId)), HttpStatus.OK);
    }

    @GetMapping(value = "/check/if/isvalid/{garantieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<Boolean>> checkIfGarantieIsValid(@NotNull @PathVariable("garantieId") Long garantieId, @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateSinistre) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Status de la garantie recupèrer avec succès!", service.isGarantieValide(garantieId, dateSinistre)), HttpStatus.OK);
    }

    @GetMapping(value = "/check/if/plafond/atteint/{souscriptionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<Boolean>> checkIfPlafondAssureAtteint(@NotNull @PathVariable("souscriptionId") Long souscriptionId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Plafond de la garantie recupèrer avec succès!", service.isPlafondAssureAtteint(souscriptionId)), HttpStatus.OK);
    }

    @GetMapping(value = "/calculate/montant/assure/{garantieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<BigDecimal>> calculateMontantAssure(@NotNull @PathVariable("garantieId") Long garantieId, @NotNull @RequestParam BigDecimal montantSinistre) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Montant assuré part la garantie pour votre sinistre recupèrer avec succès!", service.calculMontantAssure(garantieId, montantSinistre)), HttpStatus.OK);
    }
}

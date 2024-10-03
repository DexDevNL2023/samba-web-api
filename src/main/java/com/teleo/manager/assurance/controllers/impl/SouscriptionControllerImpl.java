package com.teleo.manager.assurance.controllers.impl;

import com.teleo.manager.assurance.controllers.SouscriptionController;
import com.teleo.manager.assurance.dto.reponse.SouscriptionResponse;
import com.teleo.manager.assurance.dto.request.SouscriptionRequest;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.assurance.services.SouscriptionService;
import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.prestation.dto.reponse.RegistrantResponse;
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
@RequestMapping("/api/souscriptions")
@Tag(name = "Souscriptions", description = "API de gestion des souscriptions")
public class SouscriptionControllerImpl extends ControllerGenericImpl<SouscriptionRequest, SouscriptionResponse, Souscription> implements SouscriptionController {

    private static final String MODULE_NAME = "SOUSCRIPTION_MODULE";

    private final SouscriptionService service;
    private final AuthorizationService authorizationService;

    public SouscriptionControllerImpl(SouscriptionService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Souscription newInstance() {
        return new Souscription();
    }

    @GetMapping(value = "/find/by/assure/{assureId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<SouscriptionResponse>>> getAllByAssureId(@NotNull @PathVariable("assureId") Long assureId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Souscriptions récupérées avec succès!", service.findAllByAssureId(assureId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/police/{policeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<SouscriptionResponse>>> getAllByPoliceId(@NotNull @PathVariable("policeId") Long policeId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Souscriptions récupérées avec succès!", service.findAllByPoliceId(policeId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/contrat/{contratId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<SouscriptionResponse>> getWithContratsById(@NotNull @PathVariable("contratId") Long contratId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Souscription avec contrats récupérée avec succès!", service.findWithContratsById(contratId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/sinistre/{sinistreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<SouscriptionResponse>> getWithSinistresById(@NotNull @PathVariable("sinistreId") Long sinistreId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Souscription avec sinistres récupérée avec succès!", service.findWithSinistresById(sinistreId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/prestation/{prestationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<SouscriptionResponse>> getWithPrestationsById(@NotNull @PathVariable("prestationId") Long prestationId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Souscription avec prestations récupérée avec succès!", service.findWithPrestationsById(prestationId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/paiement/{paiementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<SouscriptionResponse>> getWithPaiementsById(@NotNull @PathVariable("paiementId") Long paiementId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Souscription avec paiements récupérée avec succès!", service.findWithPaiementsById(paiementId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/reclamation/{reclamationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<SouscriptionResponse>> getWithReclamationsById(@NotNull @PathVariable("reclamationId") Long reclamationId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Souscription avec réclamations récupérée avec succès!", service.findWithReclamationsById(reclamationId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<SouscriptionResponse>>> getAllByUserId(@NotNull @PathVariable("userId") Long userId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Souscription retrouvé avec succès!", service.findByUserId(userId)), HttpStatus.OK);
    }
}

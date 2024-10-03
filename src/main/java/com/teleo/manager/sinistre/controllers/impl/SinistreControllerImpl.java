package com.teleo.manager.sinistre.controllers.impl;

import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.sinistre.controllers.SinistreController;
import com.teleo.manager.sinistre.dto.reponse.SinistreResponse;
import com.teleo.manager.sinistre.dto.request.SinistreRequest;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.sinistre.services.SinistreService;
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
@RequestMapping("/api/sinistres")
@Tag(name = "Sinistres", description = "API de gestion des sinistres")
public class SinistreControllerImpl extends ControllerGenericImpl<SinistreRequest, SinistreResponse, Sinistre> implements SinistreController {

    private static final String MODULE_NAME = "SINISTRE_MODULE";

    private final SinistreService service;
    private final AuthorizationService authorizationService;

    public SinistreControllerImpl(SinistreService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Sinistre newInstance() {
        return new Sinistre();
    }

    @GetMapping(value = "/find/by/souscription/{souscriptionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<SinistreResponse>>> getAllBySouscriptionId(@NotNull @PathVariable("souscriptionId") Long souscriptionId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Sinistres récupérés avec succès!", service.findAllBySouscriptionId(souscriptionId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/prestation/{prestationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<SinistreResponse>> getWithPrestationsById(@NotNull @PathVariable("prestationId") Long prestationId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Sinistres récupérés avec succès!", service.findWithPrestationsById(prestationId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/document/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<SinistreResponse>> getWithDocumentsById(@NotNull @PathVariable("documentId") Long documentId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Sinistres récupérés avec succès!", service.findWithDocumentsById(documentId)), HttpStatus.OK);
    }
}

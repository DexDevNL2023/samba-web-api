package com.teleo.manager.assurance.controllers.impl;

import com.teleo.manager.assurance.controllers.AssureController;
import com.teleo.manager.assurance.dto.reponse.AssureResponse;
import com.teleo.manager.assurance.dto.request.AssureRequest;
import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.assurance.services.AssureService;
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
@RequestMapping("/api/assures")
@Tag(name = "Assures", description = "API de gestion des assurés")
public class AssureControllerImpl extends ControllerGenericImpl<AssureRequest, AssureResponse, Assure> implements AssureController {

    private static final String MODULE_NAME = "ASSURE_MODULE";

    private final AssureService service;
    private final AuthorizationService authorizationService;

    protected AssureControllerImpl(AssureService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Assure newInstance() {
        return new Assure();
    }

    @GetMapping(value = "/find/by/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<AssureResponse>> getAssureByUserId(@NotNull @PathVariable("userId") Long userId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Assurés trouvés avec succès!", service.findAssureByUserId(userId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/dossier/{dossierId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<AssureResponse>> getAssuresByDossierId(@NotNull @PathVariable("dossierId") Long dossierId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Assurés trouvés avec succès!", service.findAssuresByDossierId(dossierId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/souscription/{souscriptionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<AssureResponse>> getAssuresBySouscriptionId(@NotNull @PathVariable("souscriptionId") Long souscriptionId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Assurés trouvés avec succès!", service.findAssuresBySouscriptionId(souscriptionId)), HttpStatus.OK);
    }
}

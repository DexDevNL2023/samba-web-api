package com.teleo.manager.prestation.controllers.impl;

import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.prestation.controllers.FournisseurController;
import com.teleo.manager.prestation.dto.reponse.FournisseurResponse;
import com.teleo.manager.prestation.dto.request.FournisseurRequest;
import com.teleo.manager.prestation.entities.Fournisseur;
import com.teleo.manager.prestation.services.FournisseurService;
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
@RequestMapping("/api/fournisseurs")
@Tag(name = "Fournisseurs", description = "API de gestion des fournisseurs")
public class FournisseurControllerImpl extends ControllerGenericImpl<FournisseurRequest, FournisseurResponse, Fournisseur> implements FournisseurController {

    private static final String MODULE_NAME = "FOURNISSEUR_MODULE";

    private final FournisseurService service;
    private final AuthorizationService authorizationService;

    public FournisseurControllerImpl(FournisseurService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Fournisseur newInstance() {
        return new Fournisseur();
    }

    @GetMapping(value = "/find/by/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<FournisseurResponse>> getFournisseurByUserId(@NotNull @PathVariable("userId") Long userId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Fournisseur trouvés avec succès!", service.findFournisseurByUserId(userId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/branche/{brancheId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<FournisseurResponse>>> getFournisseurWithBranchesById(@NotNull @PathVariable("brancheId") Long brancheId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Fournisseur retrouvé avec succès!", service.findFournisseurWithBranchesById(brancheId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/prestation/{prestationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<FournisseurResponse>> getFournisseurWithPrestationsById(@NotNull @PathVariable("prestationId") Long prestationId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Fournisseur retrouvé avec succès!", service.findFournisseurWithPrestationsById(prestationId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/registrant/{registrantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<FournisseurResponse>> getFournisseurWithRegistrantsById(@NotNull @PathVariable("registrantId") Long registrantId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Fournisseur retrouvé avec succès!", service.findFournisseurWithRegistrantsById(registrantId)), HttpStatus.OK);
    }
}

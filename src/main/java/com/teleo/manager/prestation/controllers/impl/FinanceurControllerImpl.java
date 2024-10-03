package com.teleo.manager.prestation.controllers.impl;

import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.prestation.controllers.FinanceurController;
import com.teleo.manager.prestation.dto.reponse.FinanceurResponse;
import com.teleo.manager.prestation.dto.reponse.PrestationResponse;
import com.teleo.manager.prestation.dto.request.FinanceurRequest;
import com.teleo.manager.prestation.entities.Financeur;
import com.teleo.manager.prestation.services.FinanceurService;
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
@RequestMapping("/api/financeurs")
@Tag(name = "Financeurs", description = "API de gestion des financeurs")
public class FinanceurControllerImpl extends ControllerGenericImpl<FinanceurRequest, FinanceurResponse, Financeur> implements FinanceurController {

    private static final String MODULE_NAME = "FINANCEUR_MODULE";

    private final FinanceurService service;
    private final AuthorizationService authorizationService;

    protected FinanceurControllerImpl(FinanceurService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Financeur newInstance() {
        return new Financeur();
    }

    @GetMapping(value = "/find/by/prestation/{prestationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<FinanceurResponse>>> getFinanceurWithPrestationsById(@NotNull @PathVariable("prestationId") Long prestationId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Financeur retrouvé avec succès!", service.findFinanceurWithPrestationsById(prestationId)), HttpStatus.OK);
    }
}

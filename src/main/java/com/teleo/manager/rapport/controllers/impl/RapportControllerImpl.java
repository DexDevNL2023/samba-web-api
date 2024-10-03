package com.teleo.manager.rapport.controllers.impl;

import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.rapport.controllers.RapportController;
import com.teleo.manager.rapport.dto.reponse.RapportResponse;
import com.teleo.manager.rapport.dto.request.RapportRequest;
import com.teleo.manager.rapport.entities.Rapport;
import com.teleo.manager.rapport.enums.RapportType;
import com.teleo.manager.rapport.services.RapportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/rapports")
@Tag(name = "Rapports", description = "API de gestion des rapports")
public class RapportControllerImpl extends ControllerGenericImpl<RapportRequest, RapportResponse, Rapport> implements RapportController {

    private static final String MODULE_NAME = "DOSSIER_MEDICAUX_MODULE";

    private final RapportService service;
    private final AuthorizationService authorizationService;

    protected RapportControllerImpl(RapportService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Rapport newInstance() {
        return new Rapport();
    }

    @GetMapping(value = "/find/by/type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<RapportResponse>>> getByType(@NotNull @PathVariable("type") RapportType type) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Rapports retrouvés avec succès!", service.findByType(type)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/date", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<RapportResponse>>> getByDateRange(
            @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Rapports retrouvés avec succès!", service.findByDateRange(startDate, endDate)), HttpStatus.OK);
    }
}

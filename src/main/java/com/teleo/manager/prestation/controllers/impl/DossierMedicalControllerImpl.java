package com.teleo.manager.prestation.controllers.impl;

import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.prestation.controllers.DossierMedicalController;
import com.teleo.manager.prestation.dto.reponse.DossierMedicalResponse;
import com.teleo.manager.prestation.dto.reponse.RegistrantResponse;
import com.teleo.manager.prestation.dto.request.DossierMedicalRequest;
import com.teleo.manager.prestation.entities.DossierMedical;
import com.teleo.manager.prestation.services.DossierMedicalService;
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
@RequestMapping("/api/dossiers/medicaux")
@Tag(name = "Dossiers médicaux", description = "API de gestion des dossiers médicaux")
public class DossierMedicalControllerImpl extends ControllerGenericImpl<DossierMedicalRequest, DossierMedicalResponse, DossierMedical> implements DossierMedicalController {

    private static final String MODULE_NAME = "DOSSIER_MEDICAUX_MODULE";

    private final DossierMedicalService service;
    private final AuthorizationService authorizationService;

    public DossierMedicalControllerImpl(DossierMedicalService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected DossierMedical newInstance() {
        return new DossierMedical();
    }

    @GetMapping(value = "/find/by/patient/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<DossierMedicalResponse>>> getDossierMedicalWithPatientById(@NotNull @PathVariable("patientId") Long patientId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Dossiers médicaux retrouvés avec succès!", service.findAllWithPatientById(patientId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/between/date", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<DossierMedicalResponse>>> getDossierMedicalByDateUpdatedBetween(
            @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Dossiers médicaux récupérés avec succès!", service.findAllByDateUpdatedBetween(startDate, endDate)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<DossierMedicalResponse>>> getAllByUserId(@NotNull @PathVariable("userId") Long userId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Dossiers médicaux retrouvé avec succès!", service.findByUserId(userId)), HttpStatus.OK);
    }
}

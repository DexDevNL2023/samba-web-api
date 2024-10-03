package com.teleo.manager.generic.controller;

import com.teleo.manager.assurance.dto.reponse.AssuranceResponse;
import com.teleo.manager.assurance.dto.reponse.GarantieResponse;
import com.teleo.manager.assurance.dto.reponse.PoliceAssuranceResponse;
import com.teleo.manager.assurance.dto.reponse.SouscriptionResponse;
import com.teleo.manager.assurance.dto.request.PublicSouscriptionRequest;
import com.teleo.manager.assurance.enums.*;
import com.teleo.manager.assurance.services.AssuranceService;
import com.teleo.manager.assurance.services.GarantieService;
import com.teleo.manager.assurance.services.PoliceAssuranceService;
import com.teleo.manager.assurance.services.SouscriptionService;
import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.enums.Authority;
import com.teleo.manager.authentification.enums.SocialProvider;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.enums.EnumValue;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.notification.enums.TypeNotification;
import com.teleo.manager.paiement.enums.PaymentMode;
import com.teleo.manager.paiement.enums.PaymentType;
import com.teleo.manager.prestation.enums.FinanceurType;
import com.teleo.manager.prestation.enums.PrestationStatus;
import com.teleo.manager.prestation.enums.PrestationType;
import com.teleo.manager.rapport.enums.RapportType;
import com.teleo.manager.sinistre.enums.SinistreStatus;
import com.teleo.manager.sinistre.enums.StatutReclamation;
import com.teleo.manager.sinistre.enums.TypeReclamation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/public")
@Tag(name = "Publics", description = "API de gestion des endpoints publics")
public class PublicController {

    private final PoliceAssuranceService policeAssuranceService;
    private final AssuranceService assuranceService;
    private final GarantieService garantieService;
    private final SouscriptionService souscriptionService;

    public PublicController(PoliceAssuranceService policeAssuranceService, AssuranceService assertionService, GarantieService garantieService, SouscriptionService souscriptionService) {
        this.policeAssuranceService = policeAssuranceService;
        this.assuranceService = assertionService;
        this.garantieService = garantieService;
        this.souscriptionService = souscriptionService;
    }

    @GetMapping(value = "/police/assurance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<List<PoliceAssuranceResponse>>> getAllPolicesAssurances() {
        return new ResponseEntity<>(new RessourceResponse<>("Police d'assurance retrouvée avec succès!", policeAssuranceService.getAll()), HttpStatus.OK);
    }

    @GetMapping(value = "/police/assurance/by/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<PoliceAssuranceResponse>> getPoliceAssuranceById(@NotNull @PathVariable("id") Long id) {
        return new ResponseEntity<>(new RessourceResponse<>("Police d'assurance retrouvée avec succès!", policeAssuranceService.getOne(policeAssuranceService.getById(id))), HttpStatus.OK);
    }

    @GetMapping(value = "/assurance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<List<AssuranceResponse>>> getAllAssurances() {
        return new ResponseEntity<>(new RessourceResponse<>("Assurances retrouvée avec succès!", assuranceService.getAll()), HttpStatus.OK);
    }

    @GetMapping(value = "/assurance/by/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<AssuranceResponse>> getAssuranceById(@NotNull @PathVariable("id") Long id) {
        return new ResponseEntity<>(new RessourceResponse<>("Assurance retrouvée avec succès!", assuranceService.getOne(assuranceService.getById(id))), HttpStatus.OK);
    }

    @GetMapping(value = "/assurance/by/police/{policeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<AssuranceResponse>> getAssuranceByPoliceId(@NotNull @PathVariable("policeId") Long policeId) {
        return new ResponseEntity<>(new RessourceResponse<>("Assurance retrouvée avec succès!", assuranceService.findAssuranceWithPolicesById(policeId)), HttpStatus.OK);
    }

    @GetMapping(value = "/garantie", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<List<GarantieResponse>>> getAllGaranties() {
        return new ResponseEntity<>(new RessourceResponse<>("Garanties retrouvée avec succès!", garantieService.getAll()), HttpStatus.OK);
    }

    @GetMapping(value = "/garantie/by/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<GarantieResponse>> getGarantieById(@NotNull @PathVariable("id") Long id) {
        return new ResponseEntity<>(new RessourceResponse<>("Garantie retrouvée avec succès!", garantieService.getOne(garantieService.getById(id))), HttpStatus.OK);
    }

    @GetMapping(value = "/garantie/by/police/{policeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<List<GarantieResponse>>> getGarantieByPoliceId(@NotNull @PathVariable Long policeId) {
        return new ResponseEntity<>(new RessourceResponse<>("Garantie retrouvée avec succès!", garantieService.findGarantieWithPolicesById(policeId)), HttpStatus.OK);
    }

    @PostMapping(value = "/make/souscription", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<SouscriptionResponse>> makeSouscription(@Valid @RequestBody PublicSouscriptionRequest dto) {
        return new ResponseEntity<>(new RessourceResponse<>("Souscription effectuée avec succès !", souscriptionService.makeSouscription(dto)), HttpStatus.CREATED);
    }
}

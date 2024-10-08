package com.teleo.manager.generic.controller;

import com.teleo.manager.assurance.dto.reponse.AssuranceResponse;
import com.teleo.manager.assurance.dto.reponse.GarantieResponse;
import com.teleo.manager.assurance.dto.reponse.PoliceAssuranceResponse;
import com.teleo.manager.assurance.dto.reponse.SouscriptionResponse;
import com.teleo.manager.assurance.dto.request.PublicSouscriptionRequest;
import com.teleo.manager.assurance.services.AssuranceService;
import com.teleo.manager.assurance.services.GarantieService;
import com.teleo.manager.assurance.services.PoliceAssuranceService;
import com.teleo.manager.assurance.services.SouscriptionService;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.paiement.dto.reponse.PaiementResponse;
import com.teleo.manager.paiement.dto.request.PublicPaiementRequest;
import com.teleo.manager.paiement.services.PaiementService;
import com.teleo.manager.prestation.dto.reponse.PrestationResponse;
import com.teleo.manager.prestation.dto.request.PublicPrestationRequest;
import com.teleo.manager.prestation.services.PrestationService;
import com.teleo.manager.sinistre.dto.reponse.ReclamationResponse;
import com.teleo.manager.sinistre.dto.reponse.SinistreResponse;
import com.teleo.manager.sinistre.dto.request.PublicReclamationRequest;
import com.teleo.manager.sinistre.dto.request.PublicSinistreRequest;
import com.teleo.manager.sinistre.services.ReclamationService;
import com.teleo.manager.sinistre.services.SinistreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/public")
@Tag(name = "Publics", description = "API de gestion des endpoints publics")
public class PublicController {

    private final PoliceAssuranceService policeAssuranceService;
    private final AssuranceService assuranceService;
    private final GarantieService garantieService;
    private final SouscriptionService souscriptionService;
    private final SinistreService sinistreService;
    private final PaiementService paiementService;
    private final ReclamationService reclamationService;
    private final PrestationService prestationService;

    public PublicController(PoliceAssuranceService policeAssuranceService, AssuranceService assertionService, GarantieService garantieService, SouscriptionService souscriptionService, SinistreService sinistreService, PaiementService paiementService, ReclamationService reclamationService, PrestationService prestationService) {
        this.policeAssuranceService = policeAssuranceService;
        this.assuranceService = assertionService;
        this.garantieService = garantieService;
        this.souscriptionService = souscriptionService;
        this.sinistreService = sinistreService;
        this.paiementService = paiementService;
        this.reclamationService = reclamationService;
        this.prestationService = prestationService;
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

    @PostMapping(value = "/make/declaration/sinistre", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<SinistreResponse>> makeDeclarationSinistre(@Valid @RequestBody PublicSinistreRequest dto) {
        return new ResponseEntity<>(new RessourceResponse<>("Declaration de sinistre effectuée avec succès !", sinistreService.makeDeclarationSinistre(dto)), HttpStatus.CREATED);
    }

    @PostMapping(value = "/make/paiement", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<PaiementResponse>> makePaiement(@Valid @RequestBody PublicPaiementRequest dto) {
        return new ResponseEntity<>(new RessourceResponse<>("Paiement de votre prime effectuée avec succès !", paiementService.makePaiement(dto)), HttpStatus.CREATED);
    }

    @PostMapping(value = "/make/demande/remboursement", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<ReclamationResponse>> makeDemandeRemboursement(@Valid @RequestBody PublicReclamationRequest dto) {
        return new ResponseEntity<>(new RessourceResponse<>("Demamde de remboursement effectuée avec succès !", reclamationService.makeDemandeRemboursement(dto)), HttpStatus.CREATED);
    }

    @PostMapping(value = "/make/prestation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<PrestationResponse>> makePrestation(@Valid @RequestBody PublicPrestationRequest dto) {
        return new ResponseEntity<>(new RessourceResponse<>("Prestation effectuée avec succès !", prestationService.makePrestation(dto)), HttpStatus.CREATED);
    }
}

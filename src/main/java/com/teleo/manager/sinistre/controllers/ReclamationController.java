package com.teleo.manager.sinistre.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.sinistre.dto.reponse.ReclamationResponse;
import com.teleo.manager.sinistre.dto.request.ReclamationRequest;
import com.teleo.manager.sinistre.entities.Reclamation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ReclamationController extends ControllerGeneric<ReclamationRequest, ReclamationResponse, Reclamation> {
    ResponseEntity<RessourceResponse<List<ReclamationResponse>>> getBySouscriptionId(@NotNull @PathVariable("souscriptionId") Long souscriptionId);
    ResponseEntity<RessourceResponse<List<ReclamationResponse>>> getAllBySinistreId(@NotNull @PathVariable("sinistreId") Long sinistreId);
    ResponseEntity<RessourceResponse<List<ReclamationResponse>>> getAllByPrestationId(@NotNull @PathVariable("prestationId") Long prestationId);
    ResponseEntity<RessourceResponse<ReclamationResponse>> getWithPaiementsById(@NotNull @PathVariable("paiementId") Long paiementId);
}

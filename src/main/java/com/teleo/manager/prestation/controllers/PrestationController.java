package com.teleo.manager.prestation.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.prestation.dto.reponse.PrestationResponse;
import com.teleo.manager.prestation.dto.request.PrestationRequest;
import com.teleo.manager.prestation.entities.Prestation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PrestationController extends ControllerGeneric<PrestationRequest, PrestationResponse, Prestation> {
    ResponseEntity<RessourceResponse<List<PrestationResponse>>> getByFournisseurId(@NotNull @PathVariable("fournisseurId") Long fournisseurId);
    ResponseEntity<RessourceResponse<List<PrestationResponse>>> getWithFinanceursById(@NotNull @PathVariable("financeurId") Long financeurId);
    ResponseEntity<RessourceResponse<List<PrestationResponse>>> getBySouscriptionId(@NotNull @PathVariable("souscriptionId") Long souscriptionId);
    ResponseEntity<RessourceResponse<List<PrestationResponse>>> getBySinistreId(@NotNull @PathVariable("sinistreId") Long sinistreId);
    ResponseEntity<RessourceResponse<PrestationResponse>> getWithDocumentsById(@NotNull @PathVariable("documentId") Long documentId);
}

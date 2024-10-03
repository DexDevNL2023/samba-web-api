package com.teleo.manager.paiement.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.paiement.dto.reponse.PaiementResponse;
import com.teleo.manager.paiement.dto.request.PaiementRequest;
import com.teleo.manager.paiement.entities.Paiement;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PaiementController extends ControllerGeneric<PaiementRequest, PaiementResponse, Paiement> {
    ResponseEntity<RessourceResponse<List<PaiementResponse>>> getBySouscriptionId(@NotNull @PathVariable("souscriptionId") Long souscriptionId);
    ResponseEntity<RessourceResponse<List<PaiementResponse>>> getByReclamationId(@NotNull @PathVariable("reclamationId") Long reclamationId);
    ResponseEntity<RessourceResponse<PaiementResponse>> getByRecuPaiementId(@NotNull @PathVariable("recuPaiementId") Long recuPaiementId);
}

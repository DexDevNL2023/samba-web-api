package com.teleo.manager.assurance.controllers;

import com.teleo.manager.assurance.dto.reponse.SouscriptionResponse;
import com.teleo.manager.assurance.dto.request.SouscriptionRequest;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SouscriptionController extends ControllerGeneric<SouscriptionRequest, SouscriptionResponse, Souscription> {
    ResponseEntity<RessourceResponse<List<SouscriptionResponse>>> getAllByAssureId(@NotNull @PathVariable("assureId") Long assureId);
    ResponseEntity<RessourceResponse<List<SouscriptionResponse>>> getAllByPoliceId(@NotNull @PathVariable("policeId") Long policeId);
    ResponseEntity<RessourceResponse<SouscriptionResponse>> getWithContratsById(@NotNull @PathVariable("contratId") Long contratId);
    ResponseEntity<RessourceResponse<SouscriptionResponse>> getWithSinistresById(@NotNull @PathVariable("sinistreId") Long sinistreId);
    ResponseEntity<RessourceResponse<SouscriptionResponse>> getWithPrestationsById(@NotNull @PathVariable("prestationId") Long prestationId);
    ResponseEntity<RessourceResponse<SouscriptionResponse>> getWithPaiementsById(@NotNull @PathVariable("paiementId") Long paiementId);
    ResponseEntity<RessourceResponse<SouscriptionResponse>> getWithReclamationsById(@NotNull @PathVariable("reclamationId") Long reclamationId);
    ResponseEntity<RessourceResponse<List<SouscriptionResponse>>> getAllByUserId(@NotNull @PathVariable("userId") Long userId);
}

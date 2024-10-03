package com.teleo.manager.assurance.controllers;

import com.teleo.manager.assurance.dto.reponse.GarantieResponse;
import com.teleo.manager.assurance.dto.request.GarantieRequest;
import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface GarantieController extends ControllerGeneric<GarantieRequest, GarantieResponse, Garantie> {
    ResponseEntity<RessourceResponse<List<GarantieResponse>>> getGarantieWithPolicesById(@NotNull @PathVariable("policeId") Long policeId);
    ResponseEntity<RessourceResponse<Boolean>> checkIfGarantieIsValid(@NotNull @PathVariable("garantieId") Long garantieId, @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateSinistre);
    ResponseEntity<RessourceResponse<Boolean>> checkIfPlafondAssureAtteint(@NotNull @PathVariable("souscriptionId") Long souscriptionId);
    ResponseEntity<RessourceResponse<BigDecimal>> calculateMontantAssure(@NotNull @PathVariable("garantieId") Long garantieId, @NotNull @RequestParam BigDecimal montantSinistre);
}

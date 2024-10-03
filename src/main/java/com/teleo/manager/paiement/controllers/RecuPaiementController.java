package com.teleo.manager.paiement.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.paiement.dto.reponse.PaiementResponse;
import com.teleo.manager.paiement.dto.reponse.RecuPaiementResponse;
import com.teleo.manager.paiement.dto.request.RecuPaiementRequest;
import com.teleo.manager.paiement.entities.RecuPaiement;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RecuPaiementController extends ControllerGeneric<RecuPaiementRequest, RecuPaiementResponse, RecuPaiement> {
    ResponseEntity<RessourceResponse<RecuPaiementResponse>> getRecuPaiementByNumeroRecu(@NotNull @PathVariable("numeroRecu") String numeroRecu);
    ResponseEntity<RessourceResponse<List<RecuPaiementResponse>>> getRecuPaiementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
    ResponseEntity<RessourceResponse<List<RecuPaiementResponse>>> getRecuPaiementsByMontantRange(
            @RequestParam BigDecimal minMontant,
            @RequestParam BigDecimal maxMontant);
    ResponseEntity<RessourceResponse<List<RecuPaiementResponse>>> getByPaiementId(@NotNull @PathVariable("paiementId") Long paiementId);
}

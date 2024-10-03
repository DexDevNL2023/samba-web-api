package com.teleo.manager.paiement.services;

import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.paiement.dto.reponse.RecuPaiementResponse;
import com.teleo.manager.paiement.dto.request.RecuPaiementRequest;
import com.teleo.manager.paiement.entities.RecuPaiement;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RecuPaiementService extends ServiceGeneric<RecuPaiementRequest, RecuPaiementResponse, RecuPaiement> {
    List<RecuPaiementResponse> findAllByDateRange(LocalDate startDate, LocalDate endDate);
    List<RecuPaiementResponse> findAllByMontantRange(BigDecimal minMontant, BigDecimal maxMontant);
    RecuPaiementResponse findByNumeroRecu(String numeroRecu);
    List<RecuPaiementResponse> findByPaiementId(Long paiementId);
}

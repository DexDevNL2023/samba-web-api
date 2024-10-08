package com.teleo.manager.paiement.services;

import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.paiement.dto.reponse.PaiementResponse;
import com.teleo.manager.paiement.dto.request.PaiementRequest;
import com.teleo.manager.paiement.dto.request.PublicPaiementRequest;
import com.teleo.manager.paiement.entities.Paiement;

import java.time.LocalDate;
import java.util.List;

public interface PaiementService extends ServiceGeneric<PaiementRequest, PaiementResponse, Paiement> {
    PaiementResponse findByNumeroPaiement(String numeroPaiement);
    List<PaiementResponse> findAllByDateRange(LocalDate startDate, LocalDate endDate);
    List<PaiementResponse> findAllBySouscriptionId(Long souscriptionId);
    List<PaiementResponse> findByReclamationId(Long reclamationId);
    PaiementResponse findByRecuPaiementId(Long recuPaiementId);

    PaiementResponse makePaiement(PublicPaiementRequest dto);
}

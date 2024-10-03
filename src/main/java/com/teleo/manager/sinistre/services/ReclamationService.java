package com.teleo.manager.sinistre.services;

import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.sinistre.dto.reponse.ReclamationResponse;
import com.teleo.manager.sinistre.dto.request.ReclamationRequest;
import com.teleo.manager.sinistre.entities.Reclamation;

import java.util.List;

public interface ReclamationService extends ServiceGeneric<ReclamationRequest, ReclamationResponse, Reclamation> {
    List<ReclamationResponse> findAllBySouscriptionId(Long souscriptionId);
    List<ReclamationResponse> findAllBySinistreId(Long sinistreId);
    List<ReclamationResponse> findAllByPrestationId(Long prestationId);
    ReclamationResponse findWithPaiementsById(Long paiementId);
}

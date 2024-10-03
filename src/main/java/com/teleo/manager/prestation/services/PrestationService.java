package com.teleo.manager.prestation.services;

import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.prestation.dto.reponse.PrestationResponse;
import com.teleo.manager.prestation.dto.request.PrestationRequest;
import com.teleo.manager.prestation.entities.Prestation;

import java.util.List;

public interface PrestationService extends ServiceGeneric<PrestationRequest, PrestationResponse, Prestation> {
    List<PrestationResponse> findAllByFournisseurId(Long fournisseurId);
    List<PrestationResponse> findAllBySouscriptionId(Long souscriptionId);
    List<PrestationResponse> findAllBySinistreId(Long sinistreId);
    List<PrestationResponse> findWithFinanceursById(Long financeurId);
    PrestationResponse findWithDocumentsById(Long documentId);
}

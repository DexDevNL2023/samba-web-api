package com.teleo.manager.sinistre.services;

import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.sinistre.dto.reponse.SinistreResponse;
import com.teleo.manager.sinistre.dto.request.SinistreRequest;
import com.teleo.manager.sinistre.entities.Sinistre;

import java.util.List;

public interface SinistreService extends ServiceGeneric<SinistreRequest, SinistreResponse, Sinistre> {
    List<SinistreResponse> findAllBySouscriptionId(Long souscriptionId);
    SinistreResponse findWithPrestationsById(Long prestationId);
    SinistreResponse findWithDocumentsById(Long documentId);
}

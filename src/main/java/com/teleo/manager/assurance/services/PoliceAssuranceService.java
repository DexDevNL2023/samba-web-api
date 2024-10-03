package com.teleo.manager.assurance.services;

import com.teleo.manager.assurance.dto.reponse.PoliceAssuranceResponse;
import com.teleo.manager.assurance.dto.request.PoliceAssuranceRequest;
import com.teleo.manager.assurance.entities.PoliceAssurance;
import com.teleo.manager.generic.service.ServiceGeneric;

import java.util.List;

public interface PoliceAssuranceService extends ServiceGeneric<PoliceAssuranceRequest, PoliceAssuranceResponse, PoliceAssurance> {
    List<PoliceAssuranceResponse> findAllWithAssuranceById(Long assuranceId);
    List<PoliceAssuranceResponse> findWithGarantieById(Long garantieId);
    PoliceAssuranceResponse findWithSouscriptionById(Long souscriptionId);
    PoliceAssurance savePolice(PoliceAssurance entity, String imageUrl);
    PoliceAssurance findByNumeroPolice(String numeroPolice);
}

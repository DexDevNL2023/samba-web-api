package com.teleo.manager.assurance.services;

import com.teleo.manager.assurance.dto.reponse.ContratAssuranceResponse;
import com.teleo.manager.assurance.dto.request.ContratAssuranceRequest;
import com.teleo.manager.assurance.entities.ContratAssurance;
import com.teleo.manager.generic.service.ServiceGeneric;

import java.util.List;

public interface ContratAssuranceService extends ServiceGeneric<ContratAssuranceRequest, ContratAssuranceResponse, ContratAssurance> {
    List<ContratAssuranceResponse> findContratWithSouscriptionById(Long souscriptionId);
}

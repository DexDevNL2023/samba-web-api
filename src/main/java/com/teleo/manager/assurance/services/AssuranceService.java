package com.teleo.manager.assurance.services;

import com.teleo.manager.assurance.dto.reponse.AssuranceResponse;
import com.teleo.manager.assurance.dto.request.AssuranceRequest;
import com.teleo.manager.assurance.entities.Assurance;
import com.teleo.manager.assurance.enums.InsuranceType;
import com.teleo.manager.generic.service.ServiceGeneric;

import java.util.List;

public interface AssuranceService extends ServiceGeneric<AssuranceRequest, AssuranceResponse, Assurance> {
    AssuranceResponse findAssuranceWithPolicesById(Long policeId);
    AssuranceResponse findAssurancesByType(InsuranceType type);
    Assurance findByType(InsuranceType insuranceType);
}



package com.teleo.manager.assurance.controllers;

import com.teleo.manager.assurance.dto.reponse.AssuranceResponse;
import com.teleo.manager.assurance.dto.request.AssuranceRequest;
import com.teleo.manager.assurance.entities.Assurance;
import com.teleo.manager.assurance.enums.InsuranceType;
import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AssuranceController extends ControllerGeneric<AssuranceRequest, AssuranceResponse, Assurance> {
    ResponseEntity<RessourceResponse<AssuranceResponse>> getAssuranceWithPolicesById(@NotNull @PathVariable("policeId") Long policeId);
    ResponseEntity<RessourceResponse<AssuranceResponse>> getAssurancesByType(@NotNull @PathVariable("type") InsuranceType type);
}


package com.teleo.manager.assurance.controllers;

import com.teleo.manager.assurance.dto.reponse.PoliceAssuranceResponse;
import com.teleo.manager.assurance.dto.request.PoliceAssuranceRequest;
import com.teleo.manager.assurance.entities.PoliceAssurance;
import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PoliceAssuranceController extends ControllerGeneric<PoliceAssuranceRequest, PoliceAssuranceResponse, PoliceAssurance> {
    ResponseEntity<RessourceResponse<List<PoliceAssuranceResponse>>> getAllWithAssuranceById(@NotNull @PathVariable("assuranceId") Long assuranceId);
    ResponseEntity<RessourceResponse<List<PoliceAssuranceResponse>>> getWithGarantiesById(@NotNull @PathVariable("garantieId") Long garantieId);
    ResponseEntity<RessourceResponse<PoliceAssuranceResponse>> getdWithSouscriptionsById(@NotNull @PathVariable("souscriptionId") Long souscriptionId);
}

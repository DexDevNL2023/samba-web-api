package com.teleo.manager.assurance.controllers;

import com.teleo.manager.assurance.dto.reponse.ContratAssuranceResponse;
import com.teleo.manager.assurance.dto.request.ContratAssuranceRequest;
import com.teleo.manager.assurance.entities.ContratAssurance;
import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ContratAssuranceController extends ControllerGeneric<ContratAssuranceRequest, ContratAssuranceResponse, ContratAssurance> {
    ResponseEntity<RessourceResponse<List<ContratAssuranceResponse>>> getContratsBySouscriptionId(@NotNull @PathVariable("souscriptionId") Long souscriptionId);
}

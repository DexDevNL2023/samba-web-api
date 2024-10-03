package com.teleo.manager.assurance.controllers;

import com.teleo.manager.assurance.dto.reponse.AssureResponse;
import com.teleo.manager.assurance.dto.request.AssureRequest;
import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AssureController extends ControllerGeneric<AssureRequest, AssureResponse, Assure> {
    ResponseEntity<RessourceResponse<AssureResponse>> getAssureByUserId(@NotNull @PathVariable("userId") Long userId);
    ResponseEntity<RessourceResponse<AssureResponse>> getAssuresByDossierId(@NotNull @PathVariable("dossierId") Long dossierId);
    ResponseEntity<RessourceResponse<AssureResponse>> getAssuresBySouscriptionId(@NotNull @PathVariable("souscriptionId") Long souscriptionId);
}

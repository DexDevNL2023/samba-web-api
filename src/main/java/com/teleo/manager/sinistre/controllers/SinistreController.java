package com.teleo.manager.sinistre.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.sinistre.dto.reponse.SinistreResponse;
import com.teleo.manager.sinistre.dto.request.SinistreRequest;
import com.teleo.manager.sinistre.entities.Sinistre;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SinistreController extends ControllerGeneric<SinistreRequest, SinistreResponse, Sinistre> {
    ResponseEntity<RessourceResponse<List<SinistreResponse>>> getAllBySouscriptionId(@NotNull @PathVariable("souscriptionId") Long souscriptionId);
    ResponseEntity<RessourceResponse<SinistreResponse>> getWithPrestationsById(@NotNull @PathVariable("prestationId") Long prestationId);
    ResponseEntity<RessourceResponse<SinistreResponse>> getWithDocumentsById(@NotNull @PathVariable("documentId") Long documentId);
}

package com.teleo.manager.document.controllers;

import com.teleo.manager.document.dto.reponse.DocumentResponse;
import com.teleo.manager.document.dto.request.DocumentRequest;
import com.teleo.manager.document.entities.Document;
import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface DocumentController extends ControllerGeneric<DocumentRequest, DocumentResponse, Document> {
    ResponseEntity<RessourceResponse<List<DocumentResponse>>> getAllBySinistreId(@NotNull @PathVariable("sinistreId") Long sinistreId);
    ResponseEntity<RessourceResponse<List<DocumentResponse>>> getAllByPrestationId(@NotNull @PathVariable("prestationId") Long prestationId);
}

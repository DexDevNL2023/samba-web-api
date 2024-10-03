package com.teleo.manager.parametre.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.parametre.dto.reponse.BrancheResponse;
import com.teleo.manager.parametre.dto.request.BrancheRequest;
import com.teleo.manager.parametre.entities.Branche;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface BrancheController extends ControllerGeneric<BrancheRequest, BrancheResponse, Branche> {
    ResponseEntity<RessourceResponse<BrancheResponse>> getBrancheWithRegistrantsById(@NotNull @PathVariable("registrantId") Long registrantId);
}

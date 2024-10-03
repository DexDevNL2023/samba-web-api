package com.teleo.manager.prestation.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.prestation.dto.reponse.FinanceurResponse;
import com.teleo.manager.prestation.dto.request.FinanceurRequest;
import com.teleo.manager.prestation.entities.Financeur;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface FinanceurController extends ControllerGeneric<FinanceurRequest, FinanceurResponse, Financeur> {
    ResponseEntity<RessourceResponse<List<FinanceurResponse>>> getFinanceurWithPrestationsById(@NotNull @PathVariable("prestationId") Long prestationId);
}

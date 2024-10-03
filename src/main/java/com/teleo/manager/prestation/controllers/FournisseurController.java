package com.teleo.manager.prestation.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.prestation.dto.reponse.FournisseurResponse;
import com.teleo.manager.prestation.dto.request.FournisseurRequest;
import com.teleo.manager.prestation.entities.Fournisseur;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface FournisseurController extends ControllerGeneric<FournisseurRequest, FournisseurResponse, Fournisseur> {
    ResponseEntity<RessourceResponse<List<FournisseurResponse>>> getFournisseurWithBranchesById(@NotNull @PathVariable("branchId") Long branchId);
    ResponseEntity<RessourceResponse<FournisseurResponse>> getFournisseurWithPrestationsById(@NotNull @PathVariable("prestationId") Long prestationId);
    ResponseEntity<RessourceResponse<FournisseurResponse>> getFournisseurWithRegistrantsById(@NotNull @PathVariable("registrantId") Long registrantId);
}

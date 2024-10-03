package com.teleo.manager.prestation.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.prestation.dto.reponse.LiteRegistrantResponse;
import com.teleo.manager.prestation.dto.reponse.RegistrantResponse;
import com.teleo.manager.prestation.dto.request.RegistrantRequest;
import com.teleo.manager.prestation.entities.Registrant;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface RegistrantController extends ControllerGeneric<RegistrantRequest, LiteRegistrantResponse, Registrant> {
    ResponseEntity<RessourceResponse<List<LiteRegistrantResponse>>> getAllByBrancheId(@NotNull @PathVariable("brancheId") Long brancheId);
    ResponseEntity<RessourceResponse<List<LiteRegistrantResponse>>> getAllByFournisseurId(@NotNull @PathVariable("fournisseurId") Long fournisseurId);
    ResponseEntity<RessourceResponse<LiteRegistrantResponse>> getByBrancheIdAndFournisseurId(@NotNull @PathVariable("brancheId") Long brancheId, @NotNull @PathVariable("fournisseurId") Long fournisseurId);
    ResponseEntity<RessourceResponse<LiteRegistrantResponse>> getByAssureId(@NotNull @PathVariable("assureId") Long assureId);
    ResponseEntity<RessourceResponse<RegistrantResponse>> getByUserId(@NotNull @PathVariable("userId") Long userId);
}

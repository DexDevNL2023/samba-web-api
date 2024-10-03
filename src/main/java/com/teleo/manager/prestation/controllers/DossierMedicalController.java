package com.teleo.manager.prestation.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.prestation.dto.reponse.DossierMedicalResponse;
import com.teleo.manager.prestation.dto.request.DossierMedicalRequest;
import com.teleo.manager.prestation.entities.DossierMedical;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface DossierMedicalController extends ControllerGeneric<DossierMedicalRequest, DossierMedicalResponse, DossierMedical> {
    ResponseEntity<RessourceResponse<List<DossierMedicalResponse>>> getDossierMedicalWithPatientById(@NotNull @PathVariable("patientId") Long patientId);
    ResponseEntity<RessourceResponse<List<DossierMedicalResponse>>> getDossierMedicalByDateUpdatedBetween(@NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
    ResponseEntity<RessourceResponse<List<DossierMedicalResponse>>> getAllByUserId(@NotNull @PathVariable("userId") Long userId);
}

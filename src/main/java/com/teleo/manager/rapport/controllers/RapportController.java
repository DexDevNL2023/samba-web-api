package com.teleo.manager.rapport.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.rapport.dto.reponse.RapportResponse;
import com.teleo.manager.rapport.dto.request.RapportRequest;
import com.teleo.manager.rapport.entities.Rapport;
import com.teleo.manager.rapport.enums.RapportType;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface RapportController extends ControllerGeneric<RapportRequest, RapportResponse, Rapport> {
    ResponseEntity<RessourceResponse<List<RapportResponse>>> getByType(@NotNull @PathVariable("type") RapportType type);
    ResponseEntity<RessourceResponse<List<RapportResponse>>> getByDateRange(@NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
}

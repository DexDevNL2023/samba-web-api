package com.teleo.manager.rapport.services;

import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.rapport.dto.reponse.RapportResponse;
import com.teleo.manager.rapport.dto.request.RapportRequest;
import com.teleo.manager.rapport.entities.Rapport;
import com.teleo.manager.rapport.enums.RapportType;

import java.time.LocalDate;
import java.util.List;

public interface RapportService extends ServiceGeneric<RapportRequest, RapportResponse, Rapport> {
    List<RapportResponse> findByType(RapportType type);
    List<RapportResponse> findByDateRange(LocalDate startDate, LocalDate endDate);
}

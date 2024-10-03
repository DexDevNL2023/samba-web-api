package com.teleo.manager.prestation.services;

import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.prestation.dto.reponse.DossierMedicalResponse;
import com.teleo.manager.prestation.dto.request.DossierMedicalRequest;
import com.teleo.manager.prestation.entities.DossierMedical;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public interface DossierMedicalService extends ServiceGeneric<DossierMedicalRequest, DossierMedicalResponse, DossierMedical> {
    List<DossierMedicalResponse> findAllWithPatientById(Long patientId);
    List<DossierMedicalResponse> findAllByDateUpdatedBetween(LocalDate startDate, LocalDate endDate);
    List<DossierMedicalResponse> findByUserId(Long userId);
}

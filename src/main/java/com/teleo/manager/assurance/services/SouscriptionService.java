package com.teleo.manager.assurance.services;

import com.teleo.manager.assurance.dto.reponse.SouscriptionResponse;
import com.teleo.manager.assurance.dto.request.PublicSouscriptionRequest;
import com.teleo.manager.assurance.dto.request.SouscriptionRequest;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.generic.service.ServiceGeneric;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.time.LocalDate;

public interface SouscriptionService extends ServiceGeneric<SouscriptionRequest, SouscriptionResponse, Souscription> {
    List<SouscriptionResponse> findAllByAssureId(Long assureId);
    List<SouscriptionResponse> findAllByPoliceId(Long policeId);
    SouscriptionResponse findWithContratsById(Long contratId);
    SouscriptionResponse findWithSinistresById(Long sinistreId);
    SouscriptionResponse findWithPrestationsById(Long prestationId);
    SouscriptionResponse findWithPaiementsById(Long paiementId);
    SouscriptionResponse findWithReclamationsById(Long reclamationId);
    List<SouscriptionResponse> findByUserId(Long userId);
    SouscriptionResponse makeSouscription(PublicSouscriptionRequest dto);
}

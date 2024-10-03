package com.teleo.manager.assurance.services;

import com.teleo.manager.assurance.dto.reponse.GarantieResponse;
import com.teleo.manager.assurance.dto.request.GarantieRequest;
import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.generic.service.ServiceGeneric;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface GarantieService extends ServiceGeneric<GarantieRequest, GarantieResponse, Garantie> {
    List<GarantieResponse> findGarantieWithPolicesById(Long policeId);
    boolean isPlafondAssureAtteint(Long souscriptionId);
    boolean isGarantieValide(Long garantieId, LocalDate dateSinistre);
    BigDecimal calculMontantAssure(Long garantieId, BigDecimal montantSinistre);
}

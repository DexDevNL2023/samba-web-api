package com.teleo.manager.prestation.services;

import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.prestation.dto.reponse.FinanceurResponse;
import com.teleo.manager.prestation.dto.request.FinanceurRequest;
import com.teleo.manager.prestation.entities.Financeur;

import java.util.List;

public interface FinanceurService extends ServiceGeneric<FinanceurRequest, FinanceurResponse, Financeur> {
    List<FinanceurResponse> findFinanceurWithPrestationsById(Long prestationId);
}

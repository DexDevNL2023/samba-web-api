package com.teleo.manager.parametre.services;

import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.parametre.dto.reponse.BrancheResponse;
import com.teleo.manager.parametre.dto.request.BrancheRequest;
import com.teleo.manager.parametre.entities.Branche;

import java.util.List;

public interface BrancheService extends ServiceGeneric<BrancheRequest, BrancheResponse, Branche> {
    BrancheResponse findBrancheWithRegistrantsById(Long registrantId);
}

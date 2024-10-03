package com.teleo.manager.assurance.services;

import com.teleo.manager.assurance.dto.reponse.AssureResponse;
import com.teleo.manager.assurance.dto.request.AssureRequest;
import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.generic.service.ServiceGeneric;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface AssureService extends ServiceGeneric<AssureRequest, AssureResponse, Assure> {
    AssureResponse findAssuresByDossierId(Long dossierId);
    AssureResponse findAssuresBySouscriptionId(Long souscriptionId);
    AssureResponse findAssureByUserId(Long userId);
}


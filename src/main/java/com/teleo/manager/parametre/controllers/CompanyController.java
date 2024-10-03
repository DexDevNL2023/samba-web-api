package com.teleo.manager.parametre.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.parametre.dto.reponse.CompanyResponse;
import com.teleo.manager.parametre.dto.request.CompanyRequest;
import com.teleo.manager.parametre.entities.Company;
import org.springframework.http.ResponseEntity;

public interface CompanyController extends ControllerGeneric<CompanyRequest, CompanyResponse, Company> {
    ResponseEntity<RessourceResponse<CompanyResponse>> getCurrentCompany();
}

package com.teleo.manager.parametre.services;

import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.parametre.dto.reponse.CompanyResponse;
import com.teleo.manager.parametre.dto.request.CompanyRequest;
import com.teleo.manager.parametre.entities.Company;

public interface CompanyService extends ServiceGeneric<CompanyRequest, CompanyResponse, Company> {
    CompanyResponse findFirstCompany();
}

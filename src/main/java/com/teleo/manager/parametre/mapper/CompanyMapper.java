package com.teleo.manager.parametre.mapper;

import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.parametre.dto.reponse.CompanyResponse;
import com.teleo.manager.parametre.dto.request.CompanyRequest;
import com.teleo.manager.parametre.entities.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper extends GenericMapper<CompanyRequest, CompanyResponse, Company> {
}

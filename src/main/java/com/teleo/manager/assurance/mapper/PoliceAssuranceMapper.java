package com.teleo.manager.assurance.mapper;

import com.teleo.manager.assurance.dto.reponse.PoliceAssuranceResponse;
import com.teleo.manager.assurance.dto.request.PoliceAssuranceRequest;
import com.teleo.manager.assurance.entities.PoliceAssurance;
import com.teleo.manager.assurance.services.AssuranceService;
import com.teleo.manager.assurance.services.GarantieService;
import com.teleo.manager.generic.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AssuranceService.class, GarantieService.class})
public interface PoliceAssuranceMapper extends GenericMapper<PoliceAssuranceRequest, PoliceAssuranceResponse, PoliceAssurance> {

    @Mapping(target = "souscriptions", ignore = true)
    PoliceAssurance toEntity(PoliceAssuranceRequest dto);

    @Mapping(target = "souscriptions", ignore = true)
    PoliceAssuranceResponse toDto(PoliceAssurance entity);
}

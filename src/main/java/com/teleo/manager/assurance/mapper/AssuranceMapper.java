package com.teleo.manager.assurance.mapper;

import com.teleo.manager.assurance.dto.reponse.AssuranceResponse;
import com.teleo.manager.assurance.dto.request.AssuranceRequest;
import com.teleo.manager.assurance.entities.Assurance;
import com.teleo.manager.generic.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssuranceMapper extends GenericMapper<AssuranceRequest, AssuranceResponse, Assurance> {

    @Mapping(target = "polices", ignore = true)
    Assurance toEntity(AssuranceRequest dto);

    @Mapping(target = "polices", ignore = true)
    AssuranceResponse toDto(Assurance entity);
}


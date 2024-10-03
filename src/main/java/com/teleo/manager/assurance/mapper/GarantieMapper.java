package com.teleo.manager.assurance.mapper;

import com.teleo.manager.assurance.dto.reponse.GarantieResponse;
import com.teleo.manager.assurance.dto.request.GarantieRequest;
import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.generic.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GarantieMapper extends GenericMapper<GarantieRequest, GarantieResponse, Garantie> {

    @Mapping(target = "polices", ignore = true)
    Garantie toEntity(GarantieRequest dto);

    @Mapping(target = "polices", ignore = true)
    GarantieResponse toDto(Garantie entity);
}

package com.teleo.manager.parametre.mapper;

import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.parametre.dto.reponse.BrancheResponse;
import com.teleo.manager.parametre.dto.request.BrancheRequest;
import com.teleo.manager.parametre.entities.Branche;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrancheMapper extends GenericMapper<BrancheRequest, BrancheResponse, Branche> {

    @Mapping(target = "registrants", ignore = true)
    Branche toEntity(BrancheRequest dto);

    @Mapping(target = "registrants", ignore = true)
    BrancheResponse toDto(Branche entity);
}


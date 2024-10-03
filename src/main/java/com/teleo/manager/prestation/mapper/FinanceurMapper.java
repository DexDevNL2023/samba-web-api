package com.teleo.manager.prestation.mapper;

import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.prestation.dto.reponse.FinanceurResponse;
import com.teleo.manager.prestation.dto.request.FinanceurRequest;
import com.teleo.manager.prestation.entities.Financeur;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FinanceurMapper extends GenericMapper<FinanceurRequest, FinanceurResponse, Financeur> {

    @Mapping(target = "prestations", ignore = true)
    Financeur toEntity(FinanceurRequest dto);

    @Mapping(target = "prestations", ignore = true)
    FinanceurResponse toDto(Financeur entity);
}

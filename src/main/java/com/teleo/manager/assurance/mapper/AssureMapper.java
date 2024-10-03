package com.teleo.manager.assurance.mapper;

import com.teleo.manager.assurance.dto.reponse.AssureResponse;
import com.teleo.manager.assurance.dto.request.AssureRequest;
import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.authentification.services.AccountService;
import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.prestation.services.RegistrantService;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {RegistrantService.class, AccountService.class})
public interface AssureMapper extends GenericMapper<AssureRequest, AssureResponse, Assure> {

    @Mapping(target = "dossiers", ignore = true)
    @Mapping(target = "souscriptions", ignore = true)
    Assure toEntity(AssureRequest dto);

    @Mapping(target = "dossiers", ignore = true)
    @Mapping(target = "souscriptions", ignore = true)
    AssureResponse toDto(Assure entity);
}


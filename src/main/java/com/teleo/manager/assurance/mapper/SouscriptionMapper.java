package com.teleo.manager.assurance.mapper;

import com.teleo.manager.assurance.dto.reponse.SouscriptionResponse;
import com.teleo.manager.assurance.dto.request.SouscriptionRequest;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.assurance.services.AssureService;
import com.teleo.manager.assurance.services.PoliceAssuranceService;
import com.teleo.manager.generic.mapper.GenericMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AssureService.class, PoliceAssuranceService.class})
public interface SouscriptionMapper extends GenericMapper<SouscriptionRequest, SouscriptionResponse, Souscription> {

    @Mapping(target = "contrats", ignore = true)
    @Mapping(target = "paiements", ignore = true)
    @Mapping(target = "sinistres", ignore = true)
    @Mapping(target = "reclamations", ignore = true)
    @Mapping(target = "prestations", ignore = true)
    Souscription toEntity(SouscriptionRequest dto);

    @Mapping(target = "contrats", ignore = true)
    @Mapping(target = "paiements", ignore = true)
    @Mapping(target = "sinistres", ignore = true)
    @Mapping(target = "reclamations", ignore = true)
    @Mapping(target = "prestations", ignore = true)
    SouscriptionResponse toDto(Souscription entity);
}

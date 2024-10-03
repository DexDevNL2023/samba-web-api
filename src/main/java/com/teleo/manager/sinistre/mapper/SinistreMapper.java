package com.teleo.manager.sinistre.mapper;

import com.teleo.manager.assurance.services.SouscriptionService;
import com.teleo.manager.document.services.DocumentService;
import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.prestation.services.PrestationService;
import com.teleo.manager.sinistre.dto.reponse.SinistreResponse;
import com.teleo.manager.sinistre.dto.request.SinistreRequest;
import com.teleo.manager.sinistre.entities.Sinistre;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {SouscriptionService.class, PrestationService.class, DocumentService.class})
public interface SinistreMapper extends GenericMapper<SinistreRequest, SinistreResponse, Sinistre> {

    @Mapping(target = "prestations", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Sinistre toEntity(SinistreRequest dto);

    @Mapping(target = "prestations", ignore = true)
    @Mapping(target = "documents", ignore = true)
    SinistreResponse toDto(Sinistre entity);
}

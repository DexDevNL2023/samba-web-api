package com.teleo.manager.sinistre.mapper;

import com.teleo.manager.assurance.services.SouscriptionService;
import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.prestation.services.PrestationService;
import com.teleo.manager.sinistre.dto.reponse.ReclamationResponse;
import com.teleo.manager.sinistre.dto.request.ReclamationRequest;
import com.teleo.manager.sinistre.entities.Reclamation;
import com.teleo.manager.sinistre.services.SinistreService;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {SouscriptionService.class, SinistreService.class, PrestationService.class})
public interface ReclamationMapper extends GenericMapper<ReclamationRequest, ReclamationResponse, Reclamation> {

    @Mapping(target = "paiements", ignore = true)
    Reclamation toEntity(ReclamationRequest dto);

    @Mapping(target = "paiements", ignore = true)
    ReclamationResponse toDto(Reclamation entity);
}

package com.teleo.manager.prestation.mapper;

import com.teleo.manager.assurance.services.SouscriptionService;
import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.prestation.dto.reponse.PrestationResponse;
import com.teleo.manager.prestation.dto.request.PrestationRequest;
import com.teleo.manager.prestation.entities.Prestation;
import com.teleo.manager.prestation.services.FinanceurService;
import com.teleo.manager.prestation.services.FournisseurService;
import com.teleo.manager.sinistre.services.SinistreService;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {FournisseurService.class, SouscriptionService.class, SinistreService.class, FinanceurService.class})
public interface PrestationMapper extends GenericMapper<PrestationRequest, PrestationResponse, Prestation> {

    @Mapping(target = "documents", ignore = true)
    Prestation toEntity(PrestationRequest dto);

    @Mapping(target = "documents", ignore = true)
    PrestationResponse toDto(Prestation entity);
}

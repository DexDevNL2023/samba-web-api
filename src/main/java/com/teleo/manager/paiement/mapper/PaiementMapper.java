package com.teleo.manager.paiement.mapper;

import com.teleo.manager.assurance.services.SouscriptionService;
import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.paiement.dto.reponse.PaiementResponse;
import com.teleo.manager.paiement.dto.request.PaiementRequest;
import com.teleo.manager.paiement.entities.Paiement;
import com.teleo.manager.sinistre.services.ReclamationService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SouscriptionService.class, ReclamationService.class})
public interface PaiementMapper extends GenericMapper<PaiementRequest, PaiementResponse, Paiement> {

    @Mapping(target = "recuPaiements", ignore = true)
    Paiement toEntity(PaiementRequest dto);

    @Mapping(target = "recuPaiements", ignore = true)
    PaiementResponse toDto(Paiement entity);
}

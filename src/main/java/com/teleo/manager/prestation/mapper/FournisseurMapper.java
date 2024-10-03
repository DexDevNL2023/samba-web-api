package com.teleo.manager.prestation.mapper;

import com.teleo.manager.authentification.services.AccountService;
import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.prestation.dto.reponse.FournisseurResponse;
import com.teleo.manager.prestation.dto.request.FournisseurRequest;
import com.teleo.manager.prestation.entities.Fournisseur;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AccountService.class})
public interface FournisseurMapper extends GenericMapper<FournisseurRequest, FournisseurResponse, Fournisseur> {

    @Mapping(target = "prestations", ignore = true)
    @Mapping(target = "registrants", ignore = true)
    Fournisseur toEntity(FournisseurRequest dto);

    @Mapping(target = "prestations", ignore = true)
    @Mapping(target = "registrants", ignore = true)
    FournisseurResponse toDto(Fournisseur entity);
}

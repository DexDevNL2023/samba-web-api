package com.teleo.manager.paiement.mapper;

import com.teleo.manager.paiement.dto.reponse.RecuPaiementResponse;
import com.teleo.manager.paiement.dto.request.RecuPaiementRequest;
import com.teleo.manager.paiement.entities.RecuPaiement;
import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.paiement.services.PaiementService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PaiementService.class})
public interface RecuPaiementMapper extends GenericMapper<RecuPaiementRequest, RecuPaiementResponse, RecuPaiement> {
}

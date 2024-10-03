package com.teleo.manager.assurance.mapper;

import com.teleo.manager.assurance.dto.reponse.ContratAssuranceResponse;
import com.teleo.manager.assurance.dto.request.ContratAssuranceRequest;
import com.teleo.manager.assurance.entities.ContratAssurance;
import com.teleo.manager.assurance.services.SouscriptionService;
import com.teleo.manager.generic.mapper.GenericMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {SouscriptionService.class})
public interface ContratAssuranceMapper extends GenericMapper<ContratAssuranceRequest, ContratAssuranceResponse, ContratAssurance> {
}

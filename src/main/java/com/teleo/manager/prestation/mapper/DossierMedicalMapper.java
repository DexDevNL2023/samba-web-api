package com.teleo.manager.prestation.mapper;

import com.teleo.manager.assurance.services.AssureService;
import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.prestation.dto.reponse.DossierMedicalResponse;
import com.teleo.manager.prestation.dto.request.DossierMedicalRequest;
import com.teleo.manager.prestation.entities.DossierMedical;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AssureService.class})
public interface DossierMedicalMapper extends GenericMapper<DossierMedicalRequest, DossierMedicalResponse, DossierMedical> {
}

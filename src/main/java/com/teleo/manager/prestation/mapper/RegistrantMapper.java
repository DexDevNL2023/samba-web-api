package com.teleo.manager.prestation.mapper;

import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.parametre.services.BrancheService;
import com.teleo.manager.prestation.dto.reponse.LiteRegistrantResponse;
import com.teleo.manager.prestation.dto.reponse.RegistrantResponse;
import com.teleo.manager.prestation.dto.request.RegistrantRequest;
import com.teleo.manager.prestation.entities.Registrant;
import com.teleo.manager.prestation.services.FournisseurService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BrancheService.class, FournisseurService.class})
public interface RegistrantMapper extends GenericMapper<RegistrantRequest, LiteRegistrantResponse, Registrant> {
    RegistrantResponse mapTo(Registrant registrant);
}

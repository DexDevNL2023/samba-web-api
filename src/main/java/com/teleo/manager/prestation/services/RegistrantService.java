package com.teleo.manager.prestation.services;

import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.prestation.dto.reponse.LiteRegistrantResponse;
import com.teleo.manager.prestation.dto.reponse.RegistrantResponse;
import com.teleo.manager.prestation.dto.request.RegistrantRequest;
import com.teleo.manager.prestation.entities.Registrant;

import java.util.List;

public interface RegistrantService extends ServiceGeneric<RegistrantRequest, LiteRegistrantResponse, Registrant> {
    List<LiteRegistrantResponse> findAllByBrancheId(Long brancheId);
    List<LiteRegistrantResponse> findAllByFournisseurId(Long fournisseurId);
    LiteRegistrantResponse findByBrancheIdAndFournisseurId(Long brancheId, Long fournisseurId);
    LiteRegistrantResponse findByAssureId(Long assureId);
    RegistrantResponse findByUserId(Long userId);
}

package com.teleo.manager.prestation.services.impl;

import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.assurance.repositories.AssureRepository;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.prestation.dto.reponse.LiteRegistrantResponse;
import com.teleo.manager.prestation.dto.reponse.RegistrantResponse;
import com.teleo.manager.prestation.dto.request.RegistrantRequest;
import com.teleo.manager.prestation.entities.Registrant;
import com.teleo.manager.prestation.mapper.RegistrantMapper;
import com.teleo.manager.prestation.repositories.RegistrantRepository;
import com.teleo.manager.prestation.services.RegistrantService;
import jakarta.transaction.Transactional;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RegistrantServiceImpl extends ServiceGenericImpl<RegistrantRequest, LiteRegistrantResponse, Registrant> implements RegistrantService {

    private final RegistrantRepository repository;
    private final RegistrantMapper mapper;
    private final AssureRepository assureRepository;

    public RegistrantServiceImpl(RegistrantRepository repository, RegistrantMapper mapper, AssureRepository assureRepository) {
        super(Registrant.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.assureRepository = assureRepository;
    }

    @Transactional
    @LogExecution
    @Override
    public List<LiteRegistrantResponse> findAllByBrancheId(Long brancheId) {
        return mapper.toDto(repository.findAllRegistrantWithBrancheById(brancheId));
    }

    @Transactional
    @LogExecution
    @Override
    public List<LiteRegistrantResponse> findAllByFournisseurId(Long fournisseurId) {
        return mapper.toDto(repository.findAllRegistrantWithFournisseurById(fournisseurId));
    }

    @Transactional
    @LogExecution
    @Override
    public LiteRegistrantResponse findByBrancheIdAndFournisseurId(Long brancheId, Long fournisseurId) {
        Registrant registrant = repository.findRegistrantByBrancheIdAndFournisseurId(brancheId, fournisseurId)
                .orElseThrow(() -> new ResourceNotFoundException("Registrant not found with brancheId=" + brancheId + " and fournisseurId=" + fournisseurId));
        return mapper.toDto(registrant);
    }

    @Override
    public LiteRegistrantResponse findByAssureId(Long assureId) {
        Assure assure = assureRepository.findById(assureId)
                .orElseThrow(() -> new ResourceNotFoundException("Assuré avec l'ID " + assureId + " introuvable"));

        Registrant registrant = repository.findById(assure.getRegistrant().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Registrant avec l'ID " + assure.getRegistrant().getId() + " introuvable"));
        return mapper.toDto(registrant);
    }

    @Override
    public RegistrantResponse findByUserId(Long userId) {
        Assure assure = assureRepository.findAssureByAccountId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Assuré avec l'ID du compte " + userId + " introuvable"));

        Registrant registrant = repository.findById(assure.getRegistrant().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Registrant avec l'ID " + assure.getRegistrant().getId() + " introuvable"));
        return mapper.mapTo(registrant);
    }
}

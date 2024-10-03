package com.teleo.manager.prestation.services.impl;

import com.teleo.manager.prestation.dto.reponse.PrestationResponse;
import com.teleo.manager.prestation.dto.request.PrestationRequest;
import com.teleo.manager.prestation.entities.Prestation;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.prestation.mapper.PrestationMapper;
import com.teleo.manager.prestation.repositories.PrestationRepository;
import com.teleo.manager.prestation.services.PrestationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PrestationServiceImpl extends ServiceGenericImpl<PrestationRequest, PrestationResponse, Prestation> implements PrestationService {

    private final PrestationRepository repository;
    private final PrestationMapper mapper;

    public PrestationServiceImpl(PrestationRepository repository, PrestationMapper mapper) {
        super(Prestation.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @LogExecution
    @Override
    public List<PrestationResponse> findAllByFournisseurId(Long fournisseurId) {
        List<Prestation> prestations = repository.findAllByFournisseurId(fournisseurId);
        return mapper.toDto(prestations);
    }

    @Transactional
    @LogExecution
    @Override
    public List<PrestationResponse> findAllBySouscriptionId(Long souscriptionId) {
        List<Prestation> prestations = repository.findAllBySouscriptionId(souscriptionId);
        return mapper.toDto(prestations);
    }

    @Transactional
    @LogExecution
    @Override
    public List<PrestationResponse> findAllBySinistreId(Long sinistreId) {
        List<Prestation> prestations = repository.findAllWithSinistreById(sinistreId);
        return mapper.toDto(prestations);
    }

    @Transactional
    @LogExecution
    @Override
    public List<PrestationResponse> findWithFinanceursById(Long financeurId) {
        return mapper.toDto(repository.findWithFinanceursById(financeurId));
    }

    @Override
    public PrestationResponse findWithDocumentsById(Long documentId) {
        Prestation prestation = repository.findByIdWithDocuments(documentId)
                .orElseThrow(() -> new RessourceNotFoundException("Prestation avec l'ID document " + documentId + " introuvable"));
        return mapper.toDto(prestation);
    }
}

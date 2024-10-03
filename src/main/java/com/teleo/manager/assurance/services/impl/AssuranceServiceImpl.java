package com.teleo.manager.assurance.services.impl;

import com.teleo.manager.assurance.dto.reponse.AssuranceResponse;
import com.teleo.manager.assurance.dto.request.AssuranceRequest;
import com.teleo.manager.assurance.entities.Assurance;
import com.teleo.manager.assurance.enums.InsuranceType;
import com.teleo.manager.assurance.mapper.AssuranceMapper;
import com.teleo.manager.assurance.repositories.AssuranceRepository;
import com.teleo.manager.assurance.services.AssuranceService;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class AssuranceServiceImpl extends ServiceGenericImpl<AssuranceRequest, AssuranceResponse, Assurance> implements AssuranceService {

    private final AssuranceRepository repository;
    private final AssuranceMapper mapper;

    public AssuranceServiceImpl(AssuranceRepository repository, AssuranceMapper mapper) {
        super(Assurance.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @LogExecution
    @Override
    public AssuranceResponse findAssuranceWithPolicesById(Long policeId) {
        Assurance assurance = repository.findAssuranceWithPolicesById(policeId)
                .orElseThrow(() -> new RessourceNotFoundException("Assurance avec l'ID police : " + policeId + " introuvable"));
        return mapper.toDto(assurance);
    }

    @Transactional
    @LogExecution
    @Override
    public AssuranceResponse findAssurancesByType(InsuranceType type) {
        Assurance assurance = repository.findAssurancesByType(type)
                .orElseThrow(() -> new RessourceNotFoundException("Assurance avec le type : " + type + " introuvable"));
        return mapper.toDto(assurance);
    }

    @Override
    public Assurance findByType(InsuranceType insuranceType) {
        return repository.findAssurancesByType(insuranceType).orElse(null);
   }
}


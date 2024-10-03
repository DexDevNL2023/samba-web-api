package com.teleo.manager.assurance.services.impl;

import com.teleo.manager.assurance.dto.reponse.ContratAssuranceResponse;
import com.teleo.manager.assurance.dto.request.ContratAssuranceRequest;
import com.teleo.manager.assurance.entities.ContratAssurance;
import com.teleo.manager.assurance.mapper.ContratAssuranceMapper;
import com.teleo.manager.assurance.repositories.ContratAssuranceRepository;
import com.teleo.manager.assurance.services.ContratAssuranceService;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ContratAssuranceServiceImpl extends ServiceGenericImpl<ContratAssuranceRequest, ContratAssuranceResponse, ContratAssurance> implements ContratAssuranceService {

    private final ContratAssuranceRepository repository;
    private final ContratAssuranceMapper mapper;

    public ContratAssuranceServiceImpl(ContratAssuranceRepository repository, ContratAssuranceMapper mapper) {
        super(ContratAssurance.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @LogExecution
    @Override
    public List<ContratAssuranceResponse> findContratWithSouscriptionById(Long souscriptionId) {
        return mapper.toDto(repository.findContratWithSouscriptionById(souscriptionId));
    }
}

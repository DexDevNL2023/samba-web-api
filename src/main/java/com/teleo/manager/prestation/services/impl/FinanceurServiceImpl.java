package com.teleo.manager.prestation.services.impl;

import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.prestation.dto.reponse.FinanceurResponse;
import com.teleo.manager.prestation.dto.request.FinanceurRequest;
import com.teleo.manager.prestation.entities.Financeur;
import com.teleo.manager.prestation.mapper.FinanceurMapper;
import com.teleo.manager.prestation.repositories.FinanceurRepository;
import com.teleo.manager.prestation.services.FinanceurService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FinanceurServiceImpl extends ServiceGenericImpl<FinanceurRequest, FinanceurResponse, Financeur> implements FinanceurService {

    private final FinanceurRepository repository;
    private final FinanceurMapper mapper;

    public FinanceurServiceImpl(FinanceurRepository repository, FinanceurMapper mapper) {
        super(Financeur.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @LogExecution
    @Override
    public List<FinanceurResponse> findFinanceurWithPrestationsById(Long prestationId) {
        return mapper.toDto(repository.findFinanceurWithPrestationsById(prestationId));
    }
}

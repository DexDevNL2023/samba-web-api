package com.teleo.manager.parametre.services.impl;

import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.parametre.dto.reponse.BrancheResponse;
import com.teleo.manager.parametre.dto.request.BrancheRequest;
import com.teleo.manager.parametre.entities.Branche;
import com.teleo.manager.parametre.mapper.BrancheMapper;
import com.teleo.manager.parametre.repositories.BrancheRepository;
import com.teleo.manager.parametre.services.BrancheService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Transactional
public class BrancheServiceImpl extends ServiceGenericImpl<BrancheRequest, BrancheResponse, Branche> implements BrancheService {

    private final BrancheRepository repository;
    private final BrancheMapper mapper;

    public BrancheServiceImpl(BrancheRepository repository, BrancheMapper mapper) {
        super(Branche.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @LogExecution
    @Override
    public BrancheResponse findBrancheWithRegistrantsById(Long registrantId) {
        Branche branche = repository.findBrancheWithRegistrantsById(registrantId)
                .orElseThrow(() -> new RessourceNotFoundException("Branche avec ID registrant" + registrantId + " introuvable"));
        return mapper.toDto(branche);
    }

    @org.springframework.transaction.annotation.Transactional
    @LogExecution
    @Override
    public BrancheResponse getOne(Branche entity) {
        BrancheResponse dto = mapper.toDto(entity);
        dto.setRegistrants(entity.getRegistrants().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return dto;
    }
}

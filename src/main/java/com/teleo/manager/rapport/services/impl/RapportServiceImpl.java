package com.teleo.manager.rapport.services.impl;

import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.rapport.dto.reponse.RapportResponse;
import com.teleo.manager.rapport.dto.request.RapportRequest;
import com.teleo.manager.rapport.entities.Rapport;
import com.teleo.manager.rapport.enums.RapportType;
import com.teleo.manager.rapport.mapper.RapportMapper;
import com.teleo.manager.rapport.repositories.RapportRepository;
import com.teleo.manager.rapport.services.RapportService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class RapportServiceImpl extends ServiceGenericImpl<RapportRequest, RapportResponse, Rapport> implements RapportService {

    private final RapportRepository repository;
    private final RapportMapper mapper;

    public RapportServiceImpl(RapportRepository repository, RapportMapper mapper) {
        super(Rapport.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @LogExecution
    @Override
    public List<RapportResponse> findByType(RapportType type) {
        return mapper.toDto(repository.findByType(type));
    }

    @Transactional
    @LogExecution
    @Override
    public List<RapportResponse> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return mapper.toDto(repository.findByDateRange(startDate, endDate));
    }
}

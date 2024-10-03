package com.teleo.manager.paiement.services.impl;

import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.paiement.dto.reponse.RecuPaiementResponse;
import com.teleo.manager.paiement.dto.request.RecuPaiementRequest;
import com.teleo.manager.paiement.entities.RecuPaiement;
import com.teleo.manager.paiement.mapper.RecuPaiementMapper;
import com.teleo.manager.paiement.repositories.RecuPaiementRepository;
import com.teleo.manager.paiement.services.RecuPaiementService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class RecuPaiementServiceImpl extends ServiceGenericImpl<RecuPaiementRequest, RecuPaiementResponse, RecuPaiement> implements RecuPaiementService {

    private final RecuPaiementRepository repository;
    private final RecuPaiementMapper mapper;

    public RecuPaiementServiceImpl(RecuPaiementRepository repository, RecuPaiementMapper mapper) {
        super(RecuPaiement.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @LogExecution
    @Override
    public List<RecuPaiementResponse> findAllByDateRange(LocalDate startDate, LocalDate endDate) {
        return mapper.toDto(repository.findAllByDateRange(startDate, endDate));
    }

    @Transactional
    @LogExecution
    @Override
    public List<RecuPaiementResponse> findAllByMontantRange(BigDecimal minMontant, BigDecimal maxMontant) {
        return mapper.toDto(repository.findAllByMontantRange(minMontant, maxMontant));
    }

    @Override
    public RecuPaiementResponse findByNumeroRecu(String numeroRecu) {
        RecuPaiement recuPaiement = repository.findByNumeroRecu(numeroRecu)
                .orElseThrow(() -> new RessourceNotFoundException("Reçu du paiement avec reference " + numeroRecu + " introuvable"));
        return mapper.toDto(recuPaiement);
    }

    @Override
    public List<RecuPaiementResponse> findByPaiementId(Long paiementId) {
        return mapper.toDto(repository.findByPaiementId(paiementId));
    }
}

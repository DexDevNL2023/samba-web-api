package com.teleo.manager.prestation.services.impl;

import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.assurance.repositories.AssureRepository;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.prestation.dto.reponse.DossierMedicalResponse;
import com.teleo.manager.prestation.dto.request.DossierMedicalRequest;
import com.teleo.manager.prestation.entities.DossierMedical;
import com.teleo.manager.prestation.mapper.DossierMedicalMapper;
import com.teleo.manager.prestation.repositories.DossierMedicalRepository;
import com.teleo.manager.prestation.services.DossierMedicalService;
import jakarta.transaction.Transactional;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DossierMedicalServiceImpl extends ServiceGenericImpl<DossierMedicalRequest, DossierMedicalResponse, DossierMedical> implements DossierMedicalService {

    private final DossierMedicalRepository repository;
    private final DossierMedicalMapper mapper;
    private final AssureRepository assureRepository;

    public DossierMedicalServiceImpl(DossierMedicalRepository repository, DossierMedicalMapper mapper, AssureRepository assureRepository) {
        super(DossierMedical.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.assureRepository = assureRepository;
    }

    @Transactional
    @LogExecution
    @Override
    public List<DossierMedicalResponse> findAllWithPatientById(Long patientId) {
        return mapper.toDto(repository.findAllWithPatientById(patientId));
    }

    @Transactional
    @LogExecution
    @Override
    public List<DossierMedicalResponse> findAllByDateUpdatedBetween(LocalDate startDate, LocalDate endDate) {
        return mapper.toDto(repository.findAllByDateUpdatedBetween(startDate, endDate));
    }

    @Override
    public List<DossierMedicalResponse> findByUserId(Long userId) {
        Assure assure = assureRepository.findAssureByAccountId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Assuré avec l'ID du compte " + userId + " introuvable"));
        return findAllWithPatientById(assure.getId());
    }
}

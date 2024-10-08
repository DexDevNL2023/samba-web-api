package com.teleo.manager.assurance.services.impl;

import com.teleo.manager.assurance.dto.reponse.PoliceAssuranceResponse;
import com.teleo.manager.assurance.dto.request.PoliceAssuranceRequest;
import com.teleo.manager.assurance.entities.PoliceAssurance;
import com.teleo.manager.assurance.mapper.PoliceAssuranceMapper;
import com.teleo.manager.assurance.repositories.PoliceAssuranceRepository;
import com.teleo.manager.assurance.services.PoliceAssuranceService;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.exceptions.InternalException;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.ImageService;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PoliceAssuranceServiceImpl extends ServiceGenericImpl<PoliceAssuranceRequest, PoliceAssuranceResponse, PoliceAssurance> implements PoliceAssuranceService {

    private final PoliceAssuranceRepository repository;
    private final PoliceAssuranceMapper mapper;
    private final ImageService imageService;

    public PoliceAssuranceServiceImpl(PoliceAssuranceRepository repository, PoliceAssuranceMapper mapper, ImageService imageService) {
        super(PoliceAssurance.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.imageService = imageService;
    }

    @jakarta.transaction.Transactional
    @LogExecution
    @Override
    public PoliceAssuranceResponse save(PoliceAssuranceRequest dto) throws RessourceNotFoundException {
        try {
            PoliceAssurance policeAssurance = mapper.toEntity(dto);

            // Decode Base64 and save the file
            String fileUrl = imageService.saveBase64File(dto.getImageUrl());
            // On construit l'url absolue du fichier
            policeAssurance.setImageUrl(fileUrl);

            policeAssurance = repository.save(policeAssurance);
            return getOne(policeAssurance);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public PoliceAssurance savePolice(PoliceAssurance entity, String imageUrl) {
        try {
            // Decode Base64 and save the file
            String fileUrl = imageService.saveBase64File(imageUrl);
            // On construit l'url absolue du fichier
            entity.setImageUrl(fileUrl);

            entity = repository.save(entity);
            return entity;
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public PoliceAssurance findByNumeroPolice(String numeroPolice) {
        return repository.findByNumeroPolice(numeroPolice).orElse(null);
    }

    @jakarta.transaction.Transactional
    @LogExecution
    @Override
    public PoliceAssuranceResponse update(PoliceAssuranceRequest dto, Long id) throws RessourceNotFoundException {
        try {
            PoliceAssurance policeAssurance = getById(id);

            // Comparer les données du DTO pour éviter la duplication
            if (policeAssurance.equalsToDto(dto)) {
                throw new RessourceNotFoundException("La ressource réclamation avec les données suivantes : " + dto.toString() + " existe déjà");
            }

            // Mise à jour des informations de la réclamation
            policeAssurance.update(mapper.toEntity(dto));

            // Decode Base64 and save the file
            String fileUrl = imageService.saveBase64File(dto.getImageUrl());
            // On construit l'url absolue du fichier
            policeAssurance.setImageUrl(fileUrl);

            policeAssurance = repository.save(policeAssurance);
            return getOne(policeAssurance);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public List<PoliceAssuranceResponse> findAllWithAssuranceById(Long assuranceId) {
        return mapper.toDto(repository.findAllWithAssuranceById(assuranceId));
    }

    @Transactional
    @LogExecution
    @Override
    public List<PoliceAssuranceResponse> findWithGarantieById(Long garantieId) {
        return mapper.toDto(repository.findWithGarantieById(garantieId));
    }

    @Transactional
    @LogExecution
    @Override
    public PoliceAssuranceResponse findWithSouscriptionById(Long souscriptionId) {
        PoliceAssurance policeAssurance = repository.findWithSouscriptionById(souscriptionId)
                .orElseThrow(() -> new RessourceNotFoundException("Police d'assurance avec l'ID souscription " + souscriptionId + " introuvable"));
        return mapper.toDto(policeAssurance);
    }

    @Transactional
    @LogExecution
    @Override
    public PoliceAssuranceResponse getOne(PoliceAssurance entity) {
        PoliceAssuranceResponse dto = mapper.toDto(entity);
        dto.setSouscriptions(entity.getSouscriptions().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return dto;
    }
}

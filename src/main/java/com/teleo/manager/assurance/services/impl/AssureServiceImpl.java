package com.teleo.manager.assurance.services.impl;

import com.teleo.manager.assurance.dto.reponse.AssureResponse;
import com.teleo.manager.assurance.dto.request.AssureRequest;
import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.assurance.mapper.AssureMapper;
import com.teleo.manager.assurance.repositories.AssureRepository;
import com.teleo.manager.assurance.services.AssureService;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.authentification.repositories.AccountRepository;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.exceptions.InternalException;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.generic.utils.GenericUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Transactional
public class AssureServiceImpl extends ServiceGenericImpl<AssureRequest, AssureResponse, Assure> implements AssureService {

    private final AssureRepository repository;
    private final AssureMapper mapper;
    private final AccountRepository accountRepository;

    public AssureServiceImpl(AssureRepository repository, AssureMapper mapper, AccountRepository accountRepository) {
        super(Assure.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @LogExecution
    @Override
    public AssureResponse save(AssureRequest dto) throws RessourceNotFoundException {
        try {
            Assure assure = mapper.toEntity(dto);
            // Générer un NUI unique
            assure.setNumNiu(GenericUtils.generateNumNiu());
            assure = repository.save(assure);
            updateAccount(assure);
            return getOne(assure);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public AssureResponse update(AssureRequest dto, Long id) throws RessourceNotFoundException {
        try {
            Assure assure = getById(id);
            if (assure.equalsToDto(dto))
                throw new RessourceNotFoundException("La ressource fournisseur avec les données suivante : " + dto.toString() + " existe déjà");
            assure.update(mapper.toEntity(dto));
            assure = repository.save(assure);
            updateAccount(assure);
            return getOne(assure);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public Assure saveDefault(Assure assure) {
        // Générer un NUI unique
        assure.setNumNiu(GenericUtils.generateNumNiu());
        assure = repository.save(assure);
        updateAccount(assure);
        return assure;
    }

    public void updateAccount(Assure assure) {
        Account account = accountRepository.findById(assure.getAccount().getId())
                .orElseThrow(() -> new RessourceNotFoundException("Compte non trouvé"));

        // Mettre à jour les informations du compte utilisateur
        account.setFullName(assure.getFirstName() + " " + assure.getLastName());
        account.setEmail(assure.getEmail());

        // Sauvegarder les modifications du compte
        accountRepository.save(account);
    }

    @Transactional
    @LogExecution
    @Override
    public AssureResponse findAssureByUserId(Long userId) {
        Assure assure = repository.findAssureByAccountId(userId)
                .orElseThrow(() -> new RessourceNotFoundException("Assuré avec l'ID account : " + userId + " introuvable"));
        return mapper.toDto(assure);
    }

    @Transactional
    @LogExecution
    @Override
    public AssureResponse findAssuresByDossierId(Long dossierId) {
        Assure assure = repository.findAssureByDossierId(dossierId)
                .orElseThrow(() -> new RessourceNotFoundException("Assuré avec l'ID dossier : " + dossierId + " introuvable"));
        return mapper.toDto(assure);
    }

    @Transactional
    @LogExecution
    @Override
    public AssureResponse findAssuresBySouscriptionId(Long souscriptionId) {
        Assure assure = repository.findAssureBySouscriptionId(souscriptionId)
                .orElseThrow(() -> new RessourceNotFoundException("Assuré avec l'ID souscription : " + souscriptionId + " introuvable"));
        return mapper.toDto(assure);
    }

    @Transactional
    @LogExecution
    @Override
    public AssureResponse getOne(Assure entity) {
        AssureResponse dto = mapper.toDto(entity);
        dto.setDossiers(entity.getDossiers().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        dto.setSouscriptions(entity.getSouscriptions().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return dto;
    }
}


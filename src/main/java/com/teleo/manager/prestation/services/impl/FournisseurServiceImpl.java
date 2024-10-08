package com.teleo.manager.prestation.services.impl;

import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.authentification.repositories.AccountRepository;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.exceptions.InternalException;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.parametre.entities.Branche;
import com.teleo.manager.parametre.repositories.BrancheRepository;
import com.teleo.manager.prestation.dto.reponse.FournisseurResponse;
import com.teleo.manager.prestation.dto.request.FournisseurRequest;
import com.teleo.manager.prestation.entities.Fournisseur;
import com.teleo.manager.prestation.entities.Registrant;
import com.teleo.manager.prestation.mapper.FournisseurMapper;
import com.teleo.manager.prestation.repositories.FournisseurRepository;
import com.teleo.manager.prestation.services.FournisseurService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FournisseurServiceImpl extends ServiceGenericImpl<FournisseurRequest, FournisseurResponse, Fournisseur> implements FournisseurService {

    private final FournisseurRepository repository;
    private final FournisseurMapper mapper;
    private final BrancheRepository brancheRepository;
    private final AccountRepository accountRepository;

    public FournisseurServiceImpl(FournisseurRepository repository, FournisseurMapper mapper, BrancheRepository brancheRepository, AccountRepository accountRepository) {
        super(Fournisseur.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.brancheRepository = brancheRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @LogExecution
    @Override
    public FournisseurResponse save(FournisseurRequest dto) throws RessourceNotFoundException {
        try {
            Fournisseur fournisseur = mapper.toEntity(dto);
            // Construire les registrants pour chaque branche
            if (!dto.getBranches().isEmpty()) {
                List<Registrant> registrants = buildRegistrants(dto.getBranches());
                fournisseur.addRegistrants(registrants);
            }
            fournisseur = repository.save(fournisseur);
            updateAccount(fournisseur);
            return getOne(fournisseur);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public FournisseurResponse update(FournisseurRequest dto, Long id) throws RessourceNotFoundException {
        try {
            Fournisseur fournisseur = getById(id);
            if (fournisseur.equalsToDto(dto))
                throw new RessourceNotFoundException("La ressource fournisseur avec les données suivante : " + dto.toString() + " existe déjà");
            fournisseur.update(mapper.toEntity(dto));
            // Construire les registrants pour chaque branche
            if (!dto.getBranches().isEmpty()) {
                List<Registrant> registrants = buildRegistrants(dto.getBranches());
                fournisseur.addRegistrants(registrants);
            }
            fournisseur = repository.save(fournisseur);
            updateAccount(fournisseur);
            return getOne(fournisseur);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public List<FournisseurResponse> getAll() {
        return repository.findAll().stream().filter(e -> !e.getIsDeleted()).map(this::getOne).collect(Collectors.toList());
    }

    @Transactional
    @LogExecution
    @Override
    public List<FournisseurResponse> getAllByIds(List<Long> ids) {
        return repository.findAllById(ids).stream().filter(e -> !e.getIsDeleted()).map(this::getOne).collect(Collectors.toList());
    }

    @Transactional
    @LogExecution
    @Override
    public Fournisseur saveDefault(Fournisseur fournisseur, List<Long> branchIds) {
        // Construire les registrants pour chaque branche
        if (!branchIds.isEmpty()) {
            List<Registrant> registrants = buildRegistrants(branchIds);
            fournisseur.addRegistrants(registrants);
        }
        fournisseur = repository.save(fournisseur);
        updateAccount(fournisseur);
        return fournisseur;
    }

    public void updateAccount(Fournisseur fournisseur) {
        Account account = accountRepository.findById(fournisseur.getAccount().getId())
                .orElseThrow(() -> new RessourceNotFoundException("Compte non trouvé"));

        // Mettre à jour les informations du compte utilisateur
        account.setFullName(fournisseur.getNom());
        account.setEmail(fournisseur.getEmail());

        // Sauvegarder les modifications du compte
        accountRepository.save(account);
    }

    @Transactional
    @LogExecution
    @Override
    public List<FournisseurResponse> findFournisseurWithBranchesById(Long branchId) {
        return mapper.toDto(repository.findFournisseurWithBranchesById(branchId));
    }

    @Transactional
    @LogExecution
    @Override
    public FournisseurResponse findFournisseurWithPrestationsById(Long prestationId) {
        Fournisseur fournisseur = repository.findFournisseurWithPrestationsById(prestationId)
                .orElseThrow(() -> new RessourceNotFoundException("Fournisseur avec l'ID prestation " + prestationId + " introuvable"));
        // Mapper DTO
        return mapper.toDto(fournisseur);
    }

    @Transactional
    @LogExecution
    @Override
    public FournisseurResponse findFournisseurWithRegistrantsById(Long registrantId) {
        Fournisseur fournisseur = repository.findFournisseurWithRegistrantsById(registrantId)
                .orElseThrow(() -> new RessourceNotFoundException("Fournisseur avec l'ID registrant " + registrantId + " introuvable"));
        // Mapper DTO
        return mapper.toDto(fournisseur);
    }

    @Transactional
    @LogExecution
    @Override
    public FournisseurResponse findFournisseurByUserId(Long userId) {
        Fournisseur fournisseur = repository.findFournisseureByAccountId(userId)
                .orElseThrow(() -> new RessourceNotFoundException("Fournisseur avec l'ID account : " + userId + " introuvable"));
        return mapper.toDto(fournisseur);
    }

    @Transactional
    @LogExecution
    private List<Registrant> buildRegistrants(List<Long> branchIds) {
        return branchIds.stream()
                .map(branchId -> {
                    Registrant registrant = new Registrant();
                    registrant.setBranche(getBrancheById(branchId));  // Assuming Branche has a constructor with id using Branche Repository findById method
                    return registrant;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @LogExecution
    private Branche getBrancheById(Long branchId) {
        return brancheRepository.findById(branchId)
                .orElseThrow(() -> new RessourceNotFoundException("Branche avec l'ID " + branchId + " introuvable"));
    }

    @Transactional
    @LogExecution
    @Override
    public FournisseurResponse getOne(Fournisseur entity) {
        FournisseurResponse dto = mapper.toDto(entity);
        dto.setPrestations(entity.getPrestations().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        dto.setRegistrants(entity.getRegistrants().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        // Ajout des branches dans l'entité fournisseur
        dto.setBranches(entity.getRegistrants().stream()
                .map(f -> f.getBranche().getId())
                .collect(Collectors.toList()));
        return dto;
    }
}

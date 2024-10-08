package com.teleo.manager.assurance.services.impl;

import com.teleo.manager.assurance.dto.reponse.GarantieResponse;
import com.teleo.manager.assurance.dto.request.GarantieRequest;
import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.assurance.entities.PoliceAssurance;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.assurance.enums.GarantieStatus;
import com.teleo.manager.assurance.mapper.GarantieMapper;
import com.teleo.manager.assurance.repositories.GarantieRepository;
import com.teleo.manager.assurance.repositories.SouscriptionRepository;
import com.teleo.manager.assurance.services.GarantieService;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.paiement.repositories.PaiementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GarantieServiceImpl extends ServiceGenericImpl<GarantieRequest, GarantieResponse, Garantie> implements GarantieService {

    private final GarantieRepository repository;
    private final GarantieMapper mapper;
    private final PaiementRepository paiementRepository;
    private final SouscriptionRepository souscriptionRepository;

    public GarantieServiceImpl(GarantieRepository repository, GarantieMapper mapper, PaiementRepository paiementRepository, SouscriptionRepository souscriptionRepository) {
        super(Garantie.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.paiementRepository = paiementRepository;
        this.souscriptionRepository = souscriptionRepository;
    }

    @Override
    public List<GarantieResponse> findGarantieWithPolicesById(Long policeId) {
        return mapper.toDto(repository.findGarantieWithPolicesById(policeId));
    }

    @Override
    public boolean isGarantieValide(Long garantieId, LocalDate dateSinistre) {
        Garantie garantie = getById(garantieId);
        if (garantie == null || garantie.getStatus() != GarantieStatus.ACTIVEE) {
            return false;
        }
        LocalDate dateDebut = garantie.getDateDebut();
        LocalDate dateFin = garantie.getDateFin();
        if ((dateDebut == null && dateFin == null) || (dateSinistre.isAfter(dateDebut) && dateSinistre.isBefore(dateFin))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isPlafondAssureAtteint(Long souscriptionId) {
        // Récupérer la Souscription avec sa Police associée et ses Garanties
        Souscription souscription = souscriptionRepository.findById(souscriptionId)
                .orElseThrow(() -> new RessourceNotFoundException("Souscription avec l'ID " + souscriptionId + " introuvable"));

        PoliceAssurance police = souscription.getPolice();

        // Parcourir toutes les Garanties liées à cette Police
        for (Garantie garantie : police.getGaranties()) {
            BigDecimal plafondAssure = garantie.getPlafondAssure();

            // Additionner les paiements liés à la Souscription et à la Garantie
            BigDecimal totalPaiements = paiementRepository.sumTotalPaiementsBySouscriptionAndGarantie(souscriptionId, garantie.getId());

            // Si le total des paiements dépasse le plafond, retourner vrai
            if (totalPaiements != null && totalPaiements.compareTo(plafondAssure) >= 0) {
                return true;
            }
        }

        // Retourner faux si aucune Garantie n'a atteint son plafond
        return false;
    }

    @Override
    public BigDecimal calculMontantAssure(Long garantieId, BigDecimal montantSinistre) {
        Garantie garantie = getById(garantieId);
        BigDecimal percentage = BigDecimal.valueOf(garantie.getPercentage());
        return percentage.multiply(montantSinistre).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }

    @Transactional
    @LogExecution
    @Override
    public GarantieResponse getOne(Garantie entity) {
        GarantieResponse dto = mapper.toDto(entity);
        dto.setPolices(entity.getPolices().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return dto;
    }
}

package com.teleo.manager.sinistre.services.impl;

import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.assurance.repositories.SouscriptionRepository;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.exceptions.InternalException;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.generic.utils.GenericUtils;
import com.teleo.manager.notification.enums.TypeNotification;
import com.teleo.manager.notification.services.NotificationService;
import com.teleo.manager.prestation.entities.Prestation;
import com.teleo.manager.prestation.repositories.PrestationRepository;
import com.teleo.manager.sinistre.dto.reponse.ReclamationResponse;
import com.teleo.manager.sinistre.dto.request.PublicReclamationRequest;
import com.teleo.manager.sinistre.dto.request.ReclamationRequest;
import com.teleo.manager.sinistre.entities.Reclamation;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.sinistre.enums.StatutReclamation;
import com.teleo.manager.sinistre.enums.TypeReclamation;
import com.teleo.manager.sinistre.mapper.ReclamationMapper;
import com.teleo.manager.sinistre.repositories.ReclamationRepository;
import com.teleo.manager.sinistre.repositories.SinistreRepository;
import com.teleo.manager.sinistre.services.ReclamationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ReclamationServiceImpl extends ServiceGenericImpl<ReclamationRequest, ReclamationResponse, Reclamation> implements ReclamationService {

    private final ReclamationRepository repository;
    private final ReclamationMapper mapper;
    private final NotificationService notificationService;
    private final SouscriptionRepository souscriptionRepository;
    private final SinistreRepository sinistreRepository;
    private final PrestationRepository prestationRepository;

    public ReclamationServiceImpl(ReclamationRepository repository, ReclamationMapper mapper, NotificationService notificationService, SouscriptionRepository souscriptionRepository, SinistreRepository sinistreRepository, PrestationRepository prestationRepository) {
        super(Reclamation.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.notificationService = notificationService;
        this.souscriptionRepository = souscriptionRepository;
        this.sinistreRepository = sinistreRepository;
        this.prestationRepository = prestationRepository;
    }

    @Transactional
    @LogExecution
    @Override
    public ReclamationResponse save(ReclamationRequest dto) throws RessourceNotFoundException {
        try {
            Reclamation reclamation = mapper.toEntity(dto);
            reclamation.setStatus(StatutReclamation.EN_COURS);
            reclamation = repository.save(reclamation);

            // Générer les détails du reçu en fonction du type de reclamation et de la souscription
            String details = buildDetailsFromReclamation(reclamation);
            // Generer la notification de bienvenue
            notificationService.generateNotification(null,
                    reclamation.getSouscription().getAssure().getAccount(),
                    "Nouvelle Réclamation",
                    details,
                    TypeNotification.CLAIM);

            return getOne(reclamation);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public ReclamationResponse update(ReclamationRequest dto, Long id) throws RessourceNotFoundException {
        try {
            Reclamation reclamation = getById(id);

            // Comparer les données du DTO pour éviter la duplication
            if (reclamation.equalsToDto(dto)) {
                throw new RessourceNotFoundException("La ressource réclamation avec les données suivantes : " + dto.toString() + " existe déjà");
            }

            // Mise à jour des informations de la réclamation
            reclamation.update(mapper.toEntity(dto));

            // Vérifier si la réclamation est approuvée et ajuster le montant approuvé si nécessaire
            if (reclamation.getStatus() == StatutReclamation.APPROUVEE) {
                if (dto.getMontantApprouve() == null || dto.getMontantApprouve().compareTo(BigDecimal.ZERO) == 0) {
                    reclamation.setMontantApprouve(determineMontantApprouve(reclamation));
                }
            }

            reclamation = repository.save(reclamation);

            // Générer les détails de la réclamation mise à jour
            String details = buildDetailsFromReclamation(reclamation);

            // Générer la notification de mise à jour de la réclamation
            notificationService.generateNotification(null,
                    reclamation.getSouscription().getAssure().getAccount(),
                    "Notification de Réclamation",
                    details,
                    TypeNotification.CLAIM);

            return getOne(reclamation);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    private BigDecimal determineMontantApprouve(Reclamation reclamation) {
        BigDecimal montantApprouve = BigDecimal.ZERO;
        Sinistre sinistre = reclamation.getSinistre();
        if (sinistre == null) {
            return montantApprouve; // Pas de sinistre associé
        }
        Souscription souscription = sinistre.getSouscription();
        List<Garantie> garanties = souscription.getPolice().getGaranties();

        for (Garantie garantie : garanties) {
            BigDecimal plafondAssure = garantie.getPlafondAssure();
            // Convertir le pourcentage de Double à BigDecimal
            BigDecimal pourcentage = BigDecimal.valueOf(garantie.getPercentage());
            // Calculer le montant approuvé pour cette garantie
            BigDecimal montantParGarantie = plafondAssure.multiply(pourcentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

            // Ajouter le montant approuvé pour cette garantie
            montantApprouve = montantApprouve.add(montantParGarantie);
        }

        // Le montant approuvé pour la réclamation ne peut pas dépasser le montant réclamé
        return montantApprouve.min(reclamation.getMontantReclame());
    }

    // Méthode pour construire les détails du message de notification de la reclamation selon le type et du status de reclamation
    @Transactional
    @LogExecution
    private String buildDetailsFromReclamation(Reclamation reclamation) {
        // Récupérer les informations de la souscription, du sinistre, de la prestation et du type et statut de la réclamation
        StatutReclamation statut = reclamation.getStatus();
        TypeReclamation type = reclamation.getType();
        Sinistre sinistre = reclamation.getSinistre();
        Prestation prestation = reclamation.getPrestation();

        // Construire le message de base selon le type de réclamation
        String baseMessage = switch (type) {
            case SINISTRE ->
                    "Votre réclamation pour le sinistre numéro " + (sinistre != null ? sinistre.getNumeroSinistre() : "n/a");
            case PRESTATION ->
                    "Votre réclamation pour la prestation numéro " + (prestation != null ? prestation.getNumeroPrestation() : "n/a");
            default -> "Votre réclamation";
        };

        // Ajouter les détails selon le statut de la réclamation
        return switch (statut) {
            case EN_COURS ->
                    baseMessage + " est actuellement en cours de traitement. Nous vous informerons dès que nous aurons des mises à jour.";
            case APPROUVEE ->
                    baseMessage + " a été approuvée. Vous recevrez bientôt des informations sur le paiement ou la résolution.";
            case REJETEE -> baseMessage + " a été rejetée. Veuillez consulter la justification pour plus de détails.";
            default -> baseMessage + " a été reçue et est en cours de traitement.";
        };
    }

    @Transactional
    @LogExecution
    @Override
    public Reclamation saveDefault(Reclamation reclamation) {
        // Générer les détails du reçu en fonction du type de reclamation et de la souscription
        String details = buildDetailsFromReclamation(reclamation);
        // Generer la notification de bienvenue
        notificationService.generateNotification(null,
                reclamation.getSouscription().getAssure().getAccount(),
                "Nouvelle Réclamation",
                details,
                TypeNotification.CLAIM);
        return repository.save(reclamation);
    }

    @Transactional
    @LogExecution
    @Override
    public List<ReclamationResponse> findAllBySouscriptionId(Long souscriptionId) {
        return mapper.toDto(repository.findAllBySouscriptionId(souscriptionId));
    }

    @Transactional
    @LogExecution
    @Override
    public List<ReclamationResponse> findAllBySinistreId(Long sinistreId) {
        return mapper.toDto(repository.findAllBySinistreId(sinistreId));
    }

    @Transactional
    @LogExecution
    @Override
    public List<ReclamationResponse> findAllByPrestationId(Long prestationId) {
        return mapper.toDto(repository.findAllByPrestationId(prestationId));
    }

    @Override
    public ReclamationResponse findWithPaiementsById(Long paiementId) {
        Reclamation reclamation = repository.findWithPaiementsById(paiementId)
                .orElseThrow(() -> new RessourceNotFoundException("Reclamation avec l'ID paiement " + paiementId + " introuvable"));
        return mapper.toDto(reclamation);
    }

    @Transactional
    @LogExecution
    @Override
    public ReclamationResponse makeDemandeRemboursement(PublicReclamationRequest dto) {
        log.info("Démarrage de la méthode makeDemandeRemboursement avec le DTO : {}", dto);

        // Création de la réclamation
        Reclamation reclamation = new Reclamation();
        reclamation.setNumeroReclamation(GenericUtils.GenerateNumero("DECL"));
        reclamation.setType(dto.getType());
        reclamation.setDateReclamation(dto.getDateReclamation());
        reclamation.setDescription(dto.getDescription());
        reclamation.setMontantReclame(dto.getMontantReclame());
        reclamation.setStatus(StatutReclamation.EN_COURS);
        log.info("Préparation de la réclamation avec les informations : {}", reclamation);

        Souscription souscription = new Souscription();
        log.info("Initialisation de la souscription.");

        // Vérification du type de réclamation pour récupérer la souscription
        if (dto.getType() == TypeReclamation.PRESTATION) {
            log.info("Type de réclamation PRESTATION détecté.");

            Prestation prestation = prestationRepository.findById(dto.getPrestation()).orElse(null);
            log.info("Recherche de la prestation avec ID {} : {}", dto.getPrestation(), prestation);

            if (prestation != null) {
                souscription = souscriptionRepository.findById(prestation.getSouscription().getId())
                        .orElseThrow(() -> new RessourceNotFoundException(
                                "Souscription avec l'ID " + prestation.getSouscription().getId() + " introuvable"));
                reclamation.setPrestation(prestation);
                log.info("Souscription récupérée pour la prestation : {}", souscription);
            }
        } else {
            log.info("Type de réclamation SINISTRE détecté.");

            Sinistre sinistre = sinistreRepository.findById(dto.getSinistre()).orElse(null);
            log.info("Recherche du sinistre avec ID {} : {}", dto.getSinistre(), sinistre);

            if (sinistre != null) {
                souscription = souscriptionRepository.findById(sinistre.getSouscription().getId())
                        .orElseThrow(() -> new RessourceNotFoundException(
                                "Souscription avec l'ID " + sinistre.getSouscription().getId() + " introuvable"));
                reclamation.setSinistre(sinistre);
                log.info("Souscription récupérée pour le sinistre : {}", souscription);
            }
        }
        reclamation.setSouscription(souscription);

        // Sauvegarde de la réclamation
        reclamation = repository.save(reclamation);
        log.info("Réclamation sauvegardée : {}", reclamation);

        // Générer les détails du reçu en fonction du type de réclamation et de la souscription
        String details = buildDetailsFromReclamation(reclamation);
        log.info("Détails générés pour la réclamation : {}", details);

        // Générer la notification pour l'assuré
        notificationService.generateNotification(null,
                reclamation.getSouscription().getAssure().getAccount(),
                "Nouvelle Réclamation",
                details,
                TypeNotification.CLAIM);
        log.info("Notification de réclamation générée pour l'assuré : {}", reclamation.getSouscription().getAssure().getAccount());

        ReclamationResponse response = getOne(reclamation);
        log.info("Réponse finale de la réclamation : {}", response);

        return response;
    }

    @org.springframework.transaction.annotation.Transactional
    @LogExecution
    @Override
    public ReclamationResponse getOne(Reclamation entity) {
        ReclamationResponse dto = mapper.toDto(entity);
        dto.setPaiements(entity.getPaiements().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return dto;
    }
}

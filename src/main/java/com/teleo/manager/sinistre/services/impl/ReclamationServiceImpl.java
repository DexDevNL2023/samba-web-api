package com.teleo.manager.sinistre.services.impl;

import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.generic.exceptions.InternalException;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.notification.enums.TypeNotification;
import com.teleo.manager.notification.services.NotificationService;
import com.teleo.manager.prestation.entities.Prestation;
import com.teleo.manager.sinistre.dto.reponse.ReclamationResponse;
import com.teleo.manager.sinistre.dto.request.ReclamationRequest;
import com.teleo.manager.sinistre.entities.Reclamation;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.sinistre.enums.StatutReclamation;
import com.teleo.manager.sinistre.enums.TypeReclamation;
import com.teleo.manager.sinistre.mapper.ReclamationMapper;
import com.teleo.manager.sinistre.repositories.ReclamationRepository;
import com.teleo.manager.sinistre.services.ReclamationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
public class ReclamationServiceImpl extends ServiceGenericImpl<ReclamationRequest, ReclamationResponse, Reclamation> implements ReclamationService {

    private final ReclamationRepository repository;
    private final ReclamationMapper mapper;
    private final NotificationService notificationService;

    public ReclamationServiceImpl(ReclamationRepository repository, ReclamationMapper mapper, NotificationService notificationService) {
        super(Reclamation.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.notificationService = notificationService;
    }

    @Transactional
    @LogExecution
    @Override
    public ReclamationResponse save(ReclamationRequest dto) throws RessourceNotFoundException {
        try {
            Reclamation reclamation = mapper.toEntity(dto);
            reclamation.setStatus(StatutReclamation.EN_COURS);

            // Générer les détails du reçu en fonction du type de reclamation et de la souscription
            String details = buildDetailsFromReclamation(reclamation);
            // Generer la notification de bienvenue
            notificationService.generateNotification(null,
                    reclamation.getSouscription().getAssure().getAccount(),
                    "Nouvelle Réclamation",
                    details,
                    TypeNotification.CLAIM);
            reclamation = repository.save(reclamation);
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

            // Générer les détails de la réclamation mise à jour
            String details = buildDetailsFromReclamation(reclamation);

            // Générer la notification de mise à jour de la réclamation
            notificationService.generateNotification(null,
                    reclamation.getSouscription().getAssure().getAccount(),
                    "Notification de Réclamation",
                    details,
                    TypeNotification.CLAIM);

            reclamation = repository.save(reclamation);
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
        String baseMessage;
        switch (type) {
            case SINISTRE:
                baseMessage = "Votre réclamation pour le sinistre numéro " + (sinistre != null ? sinistre.getNumeroSinistre() : "n/a");
                break;
            case PRESTATION:
                baseMessage = "Votre réclamation pour la prestation numéro " + (prestation != null ? prestation.getNumeroPrestation() : "n/a");
                break;
            default:
                baseMessage = "Votre réclamation";
                break;
        }

        // Ajouter les détails selon le statut de la réclamation
        switch (statut) {
            case EN_COURS:
                return baseMessage + " est actuellement en cours de traitement. Nous vous informerons dès que nous aurons des mises à jour.";
            case APPROUVEE:
                return baseMessage + " a été approuvée. Vous recevrez bientôt des informations sur le paiement ou la résolution.";
            case REJETEE:
                return baseMessage + " a été rejetée. Veuillez consulter la justification pour plus de détails.";
            default:
                return baseMessage + " a été reçue et est en cours de traitement.";
        }
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
}

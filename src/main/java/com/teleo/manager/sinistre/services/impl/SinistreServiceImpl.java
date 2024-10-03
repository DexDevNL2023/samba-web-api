package com.teleo.manager.sinistre.services.impl;

import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.notification.services.NotificationService;
import com.teleo.manager.sinistre.dto.reponse.SinistreResponse;
import com.teleo.manager.sinistre.dto.request.SinistreRequest;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.sinistre.enums.SinistreStatus;
import com.teleo.manager.sinistre.mapper.SinistreMapper;
import com.teleo.manager.sinistre.repositories.SinistreRepository;
import com.teleo.manager.sinistre.services.SinistreService;
import com.teleo.manager.generic.exceptions.InternalException;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.notification.enums.TypeNotification;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class SinistreServiceImpl extends ServiceGenericImpl<SinistreRequest, SinistreResponse, Sinistre> implements SinistreService {

    private final SinistreRepository repository;
    private final SinistreMapper mapper;
    private final NotificationService notificationService;

    public SinistreServiceImpl(SinistreRepository repository, SinistreMapper mapper, NotificationService notificationService) {
        super(Sinistre.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.notificationService = notificationService;
    }

    @Transactional
    @LogExecution
    @Override
    public SinistreResponse save(SinistreRequest dto) throws RessourceNotFoundException {
        try {
            Sinistre sinistre = mapper.toEntity(dto);
            sinistre.setStatus(SinistreStatus.EN_COURS);

            // Contrôle du statut avant de sauvegarder et d'envoyer la notification
            if (sinistre.getStatus() != SinistreStatus.CLOTURE) {
                // Générer les détails du reçu en fonction du type de sinistre
                String details = buildDetailsFromSinistre(sinistre);
                // Générer la notification de création de sinistre
                notificationService.generateNotification(null,
                        sinistre.getSouscription().getAssure().getAccount(),
                        "Nouveau Sinistre",
                        details,
                        TypeNotification.CLAIM);
            } else {
                throw new RessourceNotFoundException("Le sinistre ne peut pas être créé avec un statut de 'Clôturé'.");
            }

            sinistre = repository.save(sinistre);
            return getOne(sinistre);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public SinistreResponse update(SinistreRequest dto, Long id) throws RessourceNotFoundException {
        try {
            Sinistre sinistre = getById(id);

            // Comparer les données du DTO pour éviter la duplication
            if (sinistre.equalsToDto(dto)) {
                throw new RessourceNotFoundException("Le sinistre avec les données suivantes existe déjà : " + dto.toString());
            }

            // Mise à jour des informations du sinistre
            sinistre.update(mapper.toEntity(dto));

            // Vérifier le statut et le montant assuré
            if (sinistre.getStatus() == SinistreStatus.APPROUVE) {
                if (dto.getMontantAssure() == null || dto.getMontantAssure().compareTo(BigDecimal.ZERO) <= 0) {
                    // Déterminer le montant assuré
                    BigDecimal montantAssure = determineMontantAssure(sinistre);
                    sinistre.setMontantAssure(montantAssure);
                } else {
                    sinistre.setMontantAssure(dto.getMontantAssure());
                }
            }

            // Contrôle du statut avant d'envoyer une notification
            if (sinistre.getStatus() != SinistreStatus.CLOTURE) {
                // Générer les détails du sinistre mis à jour
                String details = buildDetailsFromSinistre(sinistre);
                // Générer la notification de mise à jour de sinistre
                notificationService.generateNotification(null,
                        sinistre.getSouscription().getAssure().getAccount(),
                        "Mise à jour du Sinistre",
                        details,
                        TypeNotification.CLAIM);
            } else {
                throw new RessourceNotFoundException("Le sinistre ne peut pas être mis à jour avec un statut de 'Clôturé'.");
            }

            sinistre = repository.save(sinistre);
            return getOne(sinistre);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    private BigDecimal determineMontantAssure(Sinistre sinistre) {
        BigDecimal montantAssure = BigDecimal.ZERO;
        Souscription souscription = sinistre.getSouscription();
        List<Garantie> garanties = souscription.getPolice().getGaranties();

        for (Garantie garantie : garanties) {
            BigDecimal plafondAssure = garantie.getPlafondAssure();
            // Conversion du pourcentage de Double en BigDecimal
            BigDecimal pourcentage = BigDecimal.valueOf(garantie.getPercentage());
            // Calcul du montant assuré pour cette garantie
            BigDecimal montantParGarantie = plafondAssure.multiply(pourcentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

            // Ajout du montant assuré pour cette garantie
            montantAssure = montantAssure.add(montantParGarantie);
        }

        // Le montant assuré pour le sinistre ne peut pas dépasser le plafond total
        return montantAssure.min(sinistre.getMontantSinistre());
    }

    @Transactional
    @LogExecution
    private String buildDetailsFromSinistre(Sinistre sinistre) {
        // Récupérer les informations du sinistre
        String numeroSinistre = sinistre != null ? sinistre.getNumeroSinistre() : "n/a";
        LocalDate dateSurvenance = sinistre != null ? sinistre.getDateSurvenance() : null;
        LocalDate dateDeclaration = sinistre != null ? sinistre.getDateDeclaration() : null;
        LocalDate dateReglement = sinistre != null ? sinistre.getDateReglement() : null;
        BigDecimal montantAssure = sinistre != null ? sinistre.getMontantAssure() : null;
        SinistreStatus statut = sinistre.getStatus();

        // Construire les détails selon les informations du sinistre
        StringBuilder details = new StringBuilder();
        details.append("Votre sinistre (Numéro : ").append(numeroSinistre).append(") ");

        if (dateSurvenance != null) {
            details.append("a eu lieu le ").append(dateSurvenance).append(". ");
        }
        if (dateDeclaration != null) {
            details.append("Déclaré le ").append(dateDeclaration).append(". ");
        }
        if (dateReglement != null) {
            details.append("Réglé le ").append(dateReglement).append(". ");
        }
        if (montantAssure != null) {
            details.append("Montant assuré du sinistre : ").append(montantAssure).append(" . ");
        }

        // Ajouter un message basé sur le statut du sinistre
        switch (statut) {
            case EN_COURS:
                details.append("Votre sinistre est actuellement en cours de traitement.");
                break;
            case APPROUVE:
                details.append("Votre sinistre a été approuvé.");
                break;
            case REJETE:
                details.append("Votre sinistre a été rejeté.");
                break;
            case CLOTURE:
                details.append("Votre sinistre est clôturé.");
                break;
            default:
                details.append("Votre sinistre est en cours de traitement.");
        }

        return details.toString();
    }

    @Transactional
    @LogExecution
    @Override
    public List<SinistreResponse> findAllBySouscriptionId(Long souscriptionId) {
        return mapper.toDto(repository.findAllBySouscriptionId(souscriptionId));
    }

    @Transactional
    @LogExecution
    @Override
    public SinistreResponse findWithPrestationsById(Long prestationId) {
        Sinistre sinistre = repository.findByIdWithPrestations(prestationId)
                .orElseThrow(() -> new RessourceNotFoundException("Sinistre avec l'ID prestation " + prestationId + " introuvable"));
        return mapper.toDto(sinistre);
    }

    @Transactional
    @LogExecution
    @Override
    public SinistreResponse findWithDocumentsById(Long documentId) {
        Sinistre sinistre = repository.findByIdWithDocuments(documentId)
                .orElseThrow(() -> new RessourceNotFoundException("Sinistre avec l'ID document " + documentId + " introuvable"));
        return mapper.toDto(sinistre);
    }
}

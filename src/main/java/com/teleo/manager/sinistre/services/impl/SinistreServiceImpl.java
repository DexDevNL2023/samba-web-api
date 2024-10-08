package com.teleo.manager.sinistre.services.impl;

import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.assurance.repositories.AssureRepository;
import com.teleo.manager.assurance.repositories.SouscriptionRepository;
import com.teleo.manager.document.entities.Document;
import com.teleo.manager.document.repositories.DocumentRepository;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.exceptions.InternalException;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.ImageService;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.generic.utils.GenericUtils;
import com.teleo.manager.notification.enums.TypeNotification;
import com.teleo.manager.notification.services.NotificationService;
import com.teleo.manager.sinistre.dto.reponse.SinistreResponse;
import com.teleo.manager.sinistre.dto.request.PublicSinistreRequest;
import com.teleo.manager.sinistre.dto.request.SinistreRequest;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.sinistre.enums.SinistreStatus;
import com.teleo.manager.sinistre.mapper.SinistreMapper;
import com.teleo.manager.sinistre.repositories.SinistreRepository;
import com.teleo.manager.sinistre.services.SinistreService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class SinistreServiceImpl extends ServiceGenericImpl<SinistreRequest, SinistreResponse, Sinistre> implements SinistreService {

    private final SinistreRepository repository;
    private final SinistreMapper mapper;
    private final NotificationService notificationService;
    private final AssureRepository assureRepository;
    private final SouscriptionRepository souscriptionRepository;
    private final DocumentRepository documentRepository;
    private final ImageService imageService;

    public SinistreServiceImpl(SinistreRepository repository, SinistreMapper mapper, NotificationService notificationService, AssureRepository assureRepository, SouscriptionRepository souscriptionRepository, DocumentRepository documentRepository, ImageService imageService) {
        super(Sinistre.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.notificationService = notificationService;
        this.assureRepository = assureRepository;
        this.souscriptionRepository = souscriptionRepository;
        this.documentRepository = documentRepository;
        this.imageService = imageService;
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
                sinistre = repository.save(sinistre);

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
                sinistre = repository.save(sinistre);

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

    @Transactional
    @LogExecution
    @Override
    public List<SinistreResponse> findByUserId(Long userId) {
        Assure assure = assureRepository.findAssureByAccountId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Assuré avec l'ID du compte " + userId + " introuvable"));
        return mapper.toDto(repository.findAllByAssureId(assure.getId()));
    }

    @Transactional
    @LogExecution
    @Override
    public List<SinistreResponse> findByAssureId(Long assureId) {
        return mapper.toDto(repository.findAllByAssureId(assureId));
    }
/*
    @Transactional
    @LogExecution
    @Override
    public SinistreResponse makeDeclarationSinistre(PublicSinistreRequest dto) {
        Souscription souscription = souscriptionRepository.findById(dto.getSouscription())
                .orElseThrow(() -> new RessourceNotFoundException("Souscription avec l'ID " + dto.getSouscription() + " introuvable"));

        Sinistre sinistre = new Sinistre();
        // Générer un numéro unique pour du sinistre
        sinistre.setNumeroSinistre(GenericUtils.GenerateNumero("SINIS"));
        sinistre.setStatus(SinistreStatus.EN_COURS);
        sinistre.setLabel(dto.getLabel());
        sinistre.setRaison(dto.getRaison());
        sinistre.setDateSurvenance(dto.getDateSurvenance());
        sinistre.setMontantSinistre(dto.getMontantSinistre());
        sinistre.setSouscription(souscription);

        // Contrôle du statut avant de sauvegarder et d'envoyer la notification
        if (sinistre.getStatus() != SinistreStatus.CLOTURE) {
            sinistre = repository.save(sinistre);

            // pour chaque file ajouter un document
            if (dto.getDocuments() != null && !dto.getDocuments().isEmpty()) {
                Sinistre finalSinistre = sinistre;
                dto.getDocuments().forEach(docRequest -> {
                    Document document = new Document();
                    document.setNumeroDocument(GenericUtils.GenerateNumero("DOC"));
                    document.setSinistre(finalSinistre);

                    // Decode Base64 and save the file
                    String fileUrl = imageService.saveBase64File(docRequest.getUrl());

                    // On construit l'url absolue du fichier
                    document.setUrl(fileUrl);

                    // Optionnel: Si vous avez d'autres attributs à remplir dans le document
                    document.setNom(docRequest.getNom());
                    document.setUrl(docRequest.getUrl());

                    // Sauvegarder le document dans le repository
                    documentRepository.save(document);
                });
            }

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

        return getOne(sinistre);
    }*/

    @Transactional
    @LogExecution
    @Override
    public SinistreResponse makeDeclarationSinistre(PublicSinistreRequest dto) {
        log.info("Démarrage de la méthode makeDeclarationSinistre avec le DTO : {}", dto);

        // Recherche de la souscription par ID
        Souscription souscription = souscriptionRepository.findById(dto.getSouscription())
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Souscription avec l'ID " + dto.getSouscription() + " introuvable"));
        log.info("Souscription récupérée : {}", souscription);

        // Construction de l'objet Sinistre
        Sinistre sinistre = new Sinistre();
        sinistre.setNumeroSinistre(GenericUtils.GenerateNumero("SIN"));
        sinistre.setStatus(SinistreStatus.EN_COURS);
        sinistre.setLabel(dto.getLabel());
        sinistre.setRaison(dto.getRaison());
        sinistre.setDateDeclaration(LocalDate.now());
        sinistre.setDateSurvenance(dto.getDateSurvenance());
        sinistre.setMontantSinistre(dto.getMontantSinistre());
        sinistre.setSouscription(souscription);
        log.info("Sinistre préparé avec les informations : {}", sinistre);

        // Contrôle du statut avant de sauvegarder et d'envoyer la notification
        if (sinistre.getStatus() != SinistreStatus.CLOTURE) {
            sinistre = repository.save(sinistre);
            log.info("Sinistre sauvegardé avec l'ID : {}", sinistre.getId());

            // Ajout des documents si présents
            if (dto.getDocuments() != null && !dto.getDocuments().isEmpty()) {
                Sinistre finalSinistre = sinistre;
                dto.getDocuments().forEach(docRequest -> {
                    Document document = new Document();
                    document.setNumeroDocument(GenericUtils.GenerateNumero("DOC"));
                    document.setSinistre(finalSinistre);

                    // Decode Base64 and save the file
                    String fileUrl = imageService.saveBase64File(docRequest.getUrl());
                    document.setUrl(fileUrl);
                    document.setNom(docRequest.getNom());
                    log.info("Document préparé avec les informations : {}", document);

                    // Sauvegarder le document dans le repository
                    documentRepository.save(document);
                    log.info("Document sauvegardé avec l'ID : {}", document.getId());
                });
            }

            // Générer les détails du reçu en fonction du type de sinistre
            String details = buildDetailsFromSinistre(sinistre);
            log.info("Détails générés pour le sinistre : {}", details);

            // Générer la notification de création de sinistre
            notificationService.generateNotification(
                    null,
                    sinistre.getSouscription().getAssure().getAccount(),
                    "Nouveau Sinistre",
                    details,
                    TypeNotification.CLAIM
            );
            log.info("Notification de sinistre envoyée pour le compte : {}", sinistre.getSouscription().getAssure().getAccount());
        } else {
            log.error("Le sinistre ne peut pas être créé avec un statut de 'Clôturé'.");
            throw new RessourceNotFoundException("Le sinistre ne peut pas être créé avec un statut de 'Clôturé'.");
        }

        SinistreResponse response = getOne(sinistre);
        log.info("Réponse de sinistre retournée : {}", response);

        return response;
    }

    @Transactional
    @LogExecution
    @Override
    public SinistreResponse getOne(Sinistre entity) {
        SinistreResponse dto = mapper.toDto(entity);
        dto.setPrestations(entity.getPrestations().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        dto.setDocuments(entity.getDocuments().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return dto;
    }
}

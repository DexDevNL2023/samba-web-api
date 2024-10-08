package com.teleo.manager.assurance.services.impl;

import com.teleo.manager.assurance.dto.reponse.SouscriptionResponse;
import com.teleo.manager.assurance.dto.request.PublicSouscriptionRequest;
import com.teleo.manager.assurance.dto.request.SouscriptionRequest;
import com.teleo.manager.assurance.entities.*;
import com.teleo.manager.assurance.enums.ContratType;
import com.teleo.manager.assurance.enums.PaymentFrequency;
import com.teleo.manager.assurance.enums.SouscriptionStatus;
import com.teleo.manager.assurance.mapper.SouscriptionMapper;
import com.teleo.manager.assurance.repositories.AssureRepository;
import com.teleo.manager.assurance.repositories.ContratAssuranceRepository;
import com.teleo.manager.assurance.repositories.PoliceAssuranceRepository;
import com.teleo.manager.assurance.repositories.SouscriptionRepository;
import com.teleo.manager.assurance.services.SouscriptionService;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.exceptions.InternalException;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.ArtelMoneyService;
import com.teleo.manager.generic.service.MoovMoneyService;
import com.teleo.manager.generic.service.PayPalService;
import com.teleo.manager.generic.service.StripeService;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.generic.utils.GenericUtils;
import com.teleo.manager.notification.enums.TypeNotification;
import com.teleo.manager.notification.services.NotificationService;
import com.teleo.manager.paiement.entities.Paiement;
import com.teleo.manager.paiement.entities.RecuPaiement;
import com.teleo.manager.paiement.enums.PaymentType;
import com.teleo.manager.paiement.enums.RecuPaymentType;
import com.teleo.manager.paiement.repositories.PaiementRepository;
import com.teleo.manager.paiement.repositories.RecuPaiementRepository;
import com.teleo.manager.prestation.entities.Prestation;
import com.teleo.manager.prestation.enums.PrestationStatus;
import com.teleo.manager.prestation.repositories.PrestationRepository;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.sinistre.enums.SinistreStatus;
import com.teleo.manager.sinistre.repositories.SinistreRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class SouscriptionServiceImpl extends ServiceGenericImpl<SouscriptionRequest, SouscriptionResponse, Souscription> implements SouscriptionService {

    private final SouscriptionRepository repository;
    private final SouscriptionMapper mapper;
    private final NotificationService notificationService;
    private final ContratAssuranceRepository contratAssuranceRepository;
    private final AssureRepository assureRepository;
    private final PoliceAssuranceRepository policeAssuranceRepository;
    private final PaiementRepository paiementRepository;
    private final SinistreRepository sinistreRepository;
    private final PrestationRepository prestationRepository;
    private final RecuPaiementRepository recuPaiementRepository;
    private final StripeService stripeService;
    private final PayPalService payPalService;
    private final MoovMoneyService moovMoneyService;
    private final ArtelMoneyService artelMoneyService;

    public SouscriptionServiceImpl(SouscriptionRepository repository, SouscriptionMapper mapper, NotificationService notificationService, ContratAssuranceRepository contratAssuranceRepository, AssureRepository assureRepository, PoliceAssuranceRepository policeAssuranceRepository, PaiementRepository paiementRepository, SinistreRepository sinistreRepository, PrestationRepository prestationRepository, RecuPaiementRepository recuPaiementRepository, StripeService stripeService, PayPalService payPalService, MoovMoneyService moovMoneyService, ArtelMoneyService artelMoneyService) {
        super(Souscription.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.notificationService = notificationService;
        this.contratAssuranceRepository = contratAssuranceRepository;
        this.assureRepository = assureRepository;
        this.policeAssuranceRepository = policeAssuranceRepository;
        this.paiementRepository = paiementRepository;
        this.sinistreRepository = sinistreRepository;
        this.prestationRepository = prestationRepository;
        this.recuPaiementRepository = recuPaiementRepository;
        this.stripeService = stripeService;
        this.payPalService = payPalService;
        this.moovMoneyService = moovMoneyService;
        this.artelMoneyService = artelMoneyService;
    }

    @Transactional
    @LogExecution
    @Override
    public SouscriptionResponse save(SouscriptionRequest dto) throws RessourceNotFoundException {
        try {
            Souscription souscription = mapper.toEntity(dto);
            // Build Souscription
            souscription = buildSouscription(souscription);
            return getOne(souscription);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public Souscription saveDefault(Souscription souscription) {
        // Build Souscription
        souscription = buildSouscription(souscription);
        // Create the souscription
        return repository.save(souscription);
    }

    @Transactional
    @LogExecution
    private Souscription buildSouscription(Souscription souscription) {
        souscription.setStatus(SouscriptionStatus.WAITING);

        // Récupérer la police d'assurance associée
        PoliceAssurance policeAssurance = souscription.getPolice();
        if (policeAssurance == null) {
            throw new RessourceNotFoundException("Police d'assurance non trouvée pour la souscription.");
        }

        // Déterminer `montantCotisation` et `dateExpiration`
        souscription.setMontantCotisation(policeAssurance.getMontantSouscription());
        souscription.setDateExpiration(calculateExpirationDate(souscription.getDateSouscription(), policeAssurance.getDureeCouverture()));

        // Sauvegarder la souscription
        souscription = repository.save(souscription);

        // Générer les contrats basés sur les garanties
        generateContracts(souscription, policeAssurance.getGaranties());

        // Générer la notification
        String details = buildDetailsFromSouscription(souscription);
        notificationService.generateNotification(null,
                souscription.getAssure().getAccount(),
                "Nouvelle Souscription",
                details,
                TypeNotification.INFO);

        return souscription;
    }

    @Transactional
    @LogExecution
    private void generateContracts(Souscription souscription, List<Garantie> garanties) {
        log.info("Démarrage de la génération des contrats pour la souscription ID: {}", souscription.getId());

        for (Garantie garantie : garanties) {
            ContratAssurance contrat = new ContratAssurance();
            String numeroContrat = GenericUtils.GenerateNumero("CON");
            contrat.setNumeroContrat(numeroContrat);
            contrat.setDateContrat(LocalDate.now());

            log.debug("Création du contrat avec le numéro: {}", numeroContrat);

            // Conversion de String en ContratType
            String typeString = souscription.getPolice().getAssurance().getType().name();
            ContratType typeContrat;
            try {
                typeContrat = ContratType.valueOf(typeString);
                contrat.setTypeContrat(typeContrat);
                log.debug("Type de contrat défini: {}", typeContrat);
            } catch (IllegalArgumentException e) {
                log.error("Type de contrat invalide: {}", typeString);
                throw new RuntimeException("Type de contrat invalide: " + typeString);
            }

            contrat.setCouverture(garantie.getTermes());
            contrat.setConditions(souscription.getPolice().getConditions());
            contrat.setDateDebut(souscription.getDateSouscription());
            contrat.setDateFin(souscription.getDateExpiration());
            contrat.setSouscription(souscription);
            log.info("Contrat a sauvegardé : {}", contrat);

            // Sauvegarder le contrat
            try {
                contratAssuranceRepository.save(contrat);
                log.info("Contrat sauvegardé avec succès: {}", contrat.getNumeroContrat());
            } catch (Exception e) {
                log.error("Erreur lors de la sauvegarde du contrat: {}", contrat.getNumeroContrat(), e);
            }
        }

        log.info("Génération des contrats terminée pour la souscription ID: {}", souscription.getId());
    }

    /**
     * Calcule la date d'expiration en fonction de la date de souscription et de la durée en mois.
     *
     * @param dateSouscription La date de souscription du contrat.
     * @param durationInMonths La durée de la couverture en mois.
     * @return La date d'expiration calculée.
     */
    @Transactional
    @LogExecution
    public static LocalDate calculateExpirationDate(LocalDate dateSouscription, int durationInMonths) {
        return dateSouscription.plusMonths(durationInMonths);
    }

    @Transactional
    @LogExecution
    @Override
    public SouscriptionResponse update(SouscriptionRequest dto, Long id) throws RessourceNotFoundException {
        try {
            Souscription souscription = getById(id);

            // Compare the DTO data to avoid duplication
            if (souscription.equalsToDto(dto)) {
                throw new RessourceNotFoundException("La souscription avec les données suivantes existe déjà : " + dto.toString());
            }

            // Update the souscription information
            souscription.update(mapper.toEntity(dto));

            // Generate the details for the notification
            String details = buildDetailsFromSouscription(souscription);
            // Generate the notification
            notificationService.generateNotification(null,
                    souscription.getAssure().getAccount(),
                    "Mise à jour de la Souscription",
                    details,
                    TypeNotification.INFO);

            souscription = repository.save(souscription);
            return getOne(souscription);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    private String buildDetailsFromSouscription(Souscription souscription) {
        // Récupérer les détails de la souscription
        String numeroSouscription = souscription.getNumeroSouscription();
        LocalDate dateSouscription = souscription.getDateSouscription();
        LocalDate dateExpiration = souscription.getDateExpiration();
        BigDecimal montantCotisation = souscription.getMontantCotisation();
        SouscriptionStatus statut = souscription.getStatus();

        // Construire les détails basés sur les informations de la souscription
        StringBuilder details = new StringBuilder();
        details.append("Votre souscription (Numéro : ").append(numeroSouscription).append(") ");

        if (dateSouscription != null) {
            details.append("a été souscrite le ").append(dateSouscription.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))).append(". ");
        }
        if (dateExpiration != null) {
            details.append("Expire le ").append(dateExpiration.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))).append(". ");
        }
        if (montantCotisation != null) {
            details.append("Montant de la cotisation : ").append(String.format("%.2f", montantCotisation)).append(" FCFA. ");
        }

        // Ajouter un message basé sur le statut de la souscription
        switch (statut) {
            case ON_RISK:
                details.append("Votre souscription est actuellement active.");
                break;
            case WAITING:
                details.append("Votre souscription est en attente de paiement.");
                break;
            case RESILIE:
                details.append("Votre souscription a été résiliée.");
                break;
            default:
                details.append("Votre souscription est en cours de traitement.");
        }

        return details.toString();
    }

    @Transactional
    @LogExecution
    @Override
    public List<SouscriptionResponse> findAllByAssureId(Long assureId) {
        return mapper.toDto(repository.findAllByAssureId(assureId));
    }

    @Transactional
    @LogExecution
    @Override
    public List<SouscriptionResponse> findAllByPoliceId(Long policeId) {
        return mapper.toDto(repository.findAllByPoliceId(policeId));
    }

    @Transactional
    @LogExecution
    @Override
    public SouscriptionResponse findWithContratsById(Long contratId) {
        Souscription souscription = repository.findWithContratsById(contratId)
                .orElseThrow(() -> new RessourceNotFoundException("Souscription avec l'ID contrat " + contratId + " introuvable"));
        return mapper.toDto(souscription);
    }

    @Transactional
    @LogExecution
    @Override
    public SouscriptionResponse findWithSinistresById(Long sinistreId) {
        Souscription souscription = repository.findWithSinistresById(sinistreId)
                .orElseThrow(() -> new RessourceNotFoundException("Souscription avec l'ID sinistre " + sinistreId + " introuvable"));
        return mapper.toDto(souscription);
    }

    @Transactional
    @LogExecution
    @Override
    public SouscriptionResponse findWithPrestationsById(Long prestationId) {
        Souscription souscription = repository.findWithPrestationsById(prestationId)
                .orElseThrow(() -> new RessourceNotFoundException("Souscription avec l'ID prestation " + prestationId + " introuvable"));
        return mapper.toDto(souscription);
    }

    @Transactional
    @LogExecution
    @Override
    public SouscriptionResponse findWithPaiementsById(Long paiementId) {
        Souscription souscription = repository.findWithPaiementsById(paiementId)
                .orElseThrow(() -> new RessourceNotFoundException("Souscription avec l'ID paiement " + paiementId + " introuvable"));
        return mapper.toDto(souscription);
    }

    @Transactional
    @LogExecution
    @Override
    public SouscriptionResponse findWithReclamationsById(Long reclamationId) {
        Souscription souscription = repository.findWithReclamationsById(reclamationId)
                .orElseThrow(() -> new RessourceNotFoundException("Souscription avec l'ID réclamation " + reclamationId + " introuvable"));
        return mapper.toDto(souscription);
    }

    @Transactional
    @LogExecution
    @Override
    public List<SouscriptionResponse> findByUserId(Long userId) {
        Assure assure = assureRepository.findAssureByAccountId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Assuré avec l'ID du compte " + userId + " introuvable"));
        return findAllByAssureId(assure.getId());
    }

    @Transactional
    @LogExecution
    @Override
    public SouscriptionResponse makeSouscription(PublicSouscriptionRequest dto) {
        log.info("Souscription");
        Assure assure = assureRepository.findAssureByAccountId(dto.getAccount())
                .orElseThrow(() -> new RessourceNotFoundException("Assuré avec l'ID account : " + dto.getAccount() + " introuvable"));

        log.info("log 1");
        PoliceAssurance police = policeAssuranceRepository.findById(dto.getPolice())
                .orElseThrow(() -> new RessourceNotFoundException("Police d'assurance avec l'ID : " + dto.getPolice() + " introuvable"));

        log.info("log 2");
        Souscription souscription = new Souscription();
        souscription.setAssure(assure);
        souscription.setPolice(police);
        souscription.setNumeroSouscription(GenericUtils.GenerateNumero("SOU"));
        souscription.setDateSouscription(LocalDate.now());
        souscription.setFrequencePaiement(dto.getFrequencePaiement());

        log.info("log 3");
        // Build Souscription
        souscription = buildSouscription(souscription);

        // On calcul le montant effectif du paiement
        BigDecimal montantPaiement = calculeMontantTotalPrime(souscription.getMontantCotisation(), souscription.getFrequencePaiement());
        log.info("Montant total à payer : " + montantPaiement);

        log.info("log 4");
        // Si montant est superieur a 0 on creer un paiement
        if (dto.getMontant().compareTo(BigDecimal.ZERO) > 0) {
            Paiement paiement = new Paiement();
            paiement.setSouscription(souscription);
            paiement.setMontant(montantPaiement);
            paiement.setDatePaiement(LocalDate.now());
            paiement.setType(PaymentType.PRIME);
            paiement.setMode(dto.getMode());
            // Construire le paiement
            // Cela persiste également le reçu de paiement grâce à la cascade
            buildPaiement(paiement);
        }

        log.info("log 5");
        return getOne(souscription);
    }

    @Transactional
    @LogExecution
    private void buildPaiement(Paiement paiement) {
        // Générer un numéro unique pour le paiement
        paiement.setNumeroPaiement(GenericUtils.GenerateNumero("PAY"));

        // Vérifier le plafond assuré avant de traiter le paiement
        BigDecimal montantTotalPaye = BigDecimal.ZERO;
        Souscription souscription = paiement.getSouscription();

        if (souscription == null) {
            throw new RessourceNotFoundException("La souscription associe a cette operation n'existe pas.");
        }

        PoliceAssurance police = souscription.getPolice();

        if (police == null) {
            throw new RessourceNotFoundException("La police d'assurance associe a cette operation n'existe pas.");
        }

        // Récupérer les garanties associées à la police
        List<Garantie> garanties = police.getGaranties();

        // Calculer le plafond assuré total en sommant les plafonds des garanties
        BigDecimal plafondAssureTotal = garanties.stream()
                .map(Garantie::getPlafondAssure)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Récupérer les paiements déjà effectués pour cette souscription
        List<Paiement> paiements = paiementRepository.findBySouscriptionAndTypeIn(souscription, List.of(PaymentType.REMBOURSEMENT, PaymentType.PRESTATION));
        montantTotalPaye = paiements.stream()
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (montantTotalPaye.compareTo(plafondAssureTotal) >= 0) {
            // Le plafond est atteint ou dépassé
            notificationService.generateNotification(null,
                    souscription.getAssure().getAccount(),
                    "Plafond Atteint pour la Police",
                    "Le plafond assuré pour la police d'assurance n° " + police.getNumeroPolice() + " a été atteint ou insufisant pour couvrir votre sinistre.",
                    TypeNotification.PAYMENT);
            throw new RessourceNotFoundException("Le plafond assuré pour cette police a été atteint.");
        }

        // Calculer le montant restant à couvrir
        BigDecimal montantRestant = plafondAssureTotal.subtract(montantTotalPaye);
        if (paiement.getMontant().compareTo(montantRestant) > 0) {
            paiement.setMontant(montantRestant); // Effectuer un paiement partiel
        }

        // Sauvegarder le paiement
        paiementRepository.save(paiement);

        // Créer un reçu de paiement à partir des informations du paiement
        RecuPaiement recuPaiement = new RecuPaiement();
        recuPaiement.setNumeroRecu(GenericUtils.GenerateNumero("REC"));
        recuPaiement.setDateEmission(LocalDate.now()); // Date actuelle
        recuPaiement.setMontant(paiement.getMontant());
        recuPaiement.setPaiement(paiement); // Relier le paiement au reçu

        switch (paiement.getType()) {
            case PRIME :
                recuPaiement.setType(RecuPaymentType.ENCAISSEMENT);
                break;
            case REMBOURSEMENT, PRESTATION :
                recuPaiement.setType(RecuPaymentType.DECAISSEMENT);
                break;
        }

        // Générer les détails du reçu en fonction du type de paiement
        String details = buildDetailsFromPaiement(paiement);

        // Ajouter les détails au reçu
        recuPaiement.setDetails(details);

        // Sauvegarder le reçu
        recuPaiementRepository.save(recuPaiement);

        // Process payment through the chosen gateway
        boolean success = processPayment(paiement, details);

        /*if (!success) {
            throw new RessourceNotFoundException("Impossible de traiter le paiement");
        }*/

        // Si le paiement est un succès, changer le statut de l'élément associé
        switch (paiement.getType()) {
            case PRIME:
                souscription.setStatus(SouscriptionStatus.ON_RISK);
                repository.save(souscription);  // Sauvegarder la souscription mise à jour
                break;
            case REMBOURSEMENT:
                if (paiement.getReclamation() != null && paiement.getReclamation().getSinistre() != null) {
                    Sinistre sinistre = paiement.getReclamation().getSinistre();
                    sinistre.setStatus(SinistreStatus.CLOTURE);
                    sinistreRepository.save(sinistre);  // Sauvegarder le sinistre mis à jour
                }
                break;
            case PRESTATION:
                if (paiement.getReclamation() != null && paiement.getReclamation().getPrestation() != null) {
                    Prestation prestation = paiement.getReclamation().getPrestation();
                    prestation.setStatus(PrestationStatus.REMBOURSE);
                    prestationRepository.save(prestation);
                }
                break;
        }

        // Construire le titre de notification selon le type de paiement
        String titre = switch (paiement.getType()) {
            case REMBOURSEMENT, PRESTATION -> "Paiement Effectué";
            default -> "Paiement Reçu";
        };

        // Générer la notification de paiement
        notificationService.generateNotification(null,
                paiement.getSouscription().getAssure().getAccount(),
                titre,
                details,
                TypeNotification.PAYMENT);

    }

    @Transactional
    @LogExecution
    private String buildDetailsFromPaiement(Paiement paiement) {
        // Récupérer les informations de la souscription et du paiement
        Souscription souscription = paiement.getSouscription();
        if (souscription == null) {
            throw new RessourceNotFoundException("Impossible de traiter le paiement");
        }

        // Format de date souhaité
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = paiement.getDatePaiement().format(formatter);

        // Construire les détails selon le type de paiement
        return switch (paiement.getType()) {
            case PRIME -> {
                if (paiement.getSouscription() != null && paiement.getSouscription().getPolice() != null) {
                    PoliceAssurance police = souscription.getPolice();
                    yield "Paiement de la prime de la " + police.getLabel() +
                            " pour " + formattedDate + ", souscription n° " + souscription.getNumeroSouscription();
                }
                yield "Paiement de la prime d'assurance pour la souscription n° " +
                        souscription.getNumeroSouscription();
            }
            case REMBOURSEMENT -> {
                if (paiement.getReclamation() != null && paiement.getReclamation().getSinistre() != null) {
                    Sinistre sinistre = paiement.getReclamation().getSinistre();
                    yield "Remboursement après " + sinistre.getLabel() + " du " + sinistre.getDateDeclaration() +
                            " pour la souscription n° " + souscription.getNumeroSouscription();
                }
                yield "Paiement de votre sinistre pour la souscription n° " +
                        souscription.getNumeroSouscription();
            }
            case PRESTATION -> {
                if (paiement.getReclamation() != null && paiement.getReclamation().getSinistre() != null) {
                    Prestation prestation = paiement.getReclamation().getPrestation();
                    yield "Paiement pour la " + prestation.getLabel() + " du " + prestation.getDatePrestation() +
                            " liée à la souscription n° " + souscription.getNumeroSouscription();
                }
                yield "Paiement de votre prestation pour la souscription n° " +
                        souscription.getNumeroSouscription();
            }
            default -> "Paiement de la prime d'assurance pour la souscription n° " +
                    souscription.getNumeroSouscription();
        };
    }

    @Transactional
    @LogExecution
    public BigDecimal calculeMontantTotalPrime(BigDecimal montantCotisation, PaymentFrequency frequencePaiement) {
        // Par défaut, on suppose que le montant des cotisations est mensuel
        BigDecimal montantTotal = switch (frequencePaiement) {
            case MENSUEL -> montantCotisation.multiply(BigDecimal.valueOf(1)); // 1 mois
            case TRIMESTRIEL -> montantCotisation.multiply(BigDecimal.valueOf(3)); // 3 mois
            case SEMESTRIEL -> montantCotisation.multiply(BigDecimal.valueOf(6)); // 6 mois
            case ANNUEL -> montantCotisation.multiply(BigDecimal.valueOf(12)); // 12 mois
            default -> montantCotisation.multiply(BigDecimal.valueOf(1)); // Par défaut, mensuel
        };

        // Retourner le montant total à payer
        return montantTotal;
    }

    @Transactional
    @LogExecution
    private boolean processPayment(Paiement paiement, String details) {
        return switch (paiement.getMode()) {
            case STRIPE -> stripeService.processPayment(paiement, details);
            case PAYPAL -> payPalService.processPayment(paiement, details);
            case MOOV -> moovMoneyService.processPayment(paiement, details);
            case AIRTEL -> artelMoneyService.processPayment(paiement, details);
            default -> true;
        };
    }

    @Transactional
    @LogExecution
    @Override
    public SouscriptionResponse getOne(Souscription entity) {
        SouscriptionResponse dto = mapper.toDto(entity);
        dto.setContrats(entity.getContrats().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        dto.setPaiements(entity.getPaiements().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        dto.setSinistres(entity.getSinistres().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        dto.setReclamations(entity.getReclamations().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        dto.setPrestations(entity.getPrestations().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return dto;
    }
}

package com.teleo.manager.paiement.services.impl;

import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.assurance.entities.PoliceAssurance;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.assurance.enums.PaymentFrequency;
import com.teleo.manager.assurance.enums.SouscriptionStatus;
import com.teleo.manager.assurance.repositories.SouscriptionRepository;
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
import com.teleo.manager.paiement.dto.reponse.PaiementResponse;
import com.teleo.manager.paiement.dto.request.PaiementRequest;
import com.teleo.manager.paiement.dto.request.PublicPaiementRequest;
import com.teleo.manager.paiement.entities.Paiement;
import com.teleo.manager.paiement.entities.RecuPaiement;
import com.teleo.manager.paiement.enums.PaymentType;
import com.teleo.manager.paiement.enums.RecuPaymentType;
import com.teleo.manager.paiement.mapper.PaiementMapper;
import com.teleo.manager.paiement.repositories.PaiementRepository;
import com.teleo.manager.paiement.repositories.RecuPaiementRepository;
import com.teleo.manager.paiement.services.PaiementService;
import com.teleo.manager.prestation.entities.Prestation;
import com.teleo.manager.prestation.enums.PrestationStatus;
import com.teleo.manager.prestation.repositories.PrestationRepository;
import com.teleo.manager.sinistre.entities.Sinistre;
import com.teleo.manager.sinistre.enums.SinistreStatus;
import com.teleo.manager.sinistre.repositories.SinistreRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PaiementServiceImpl extends ServiceGenericImpl<PaiementRequest, PaiementResponse, Paiement> implements PaiementService {

    private final PaiementRepository repository;
    private final PaiementMapper mapper;
    private final NotificationService notificationService;
    private final StripeService stripeService;
    private final PayPalService payPalService;
    private final MoovMoneyService moovMoneyService;
    private final ArtelMoneyService artelMoneyService;
    private final SouscriptionRepository souscriptionRepository;
    private final SinistreRepository sinistreRepository;
    private final PrestationRepository prestationRepository;
    private final RecuPaiementRepository recuPaiementRepository;

    public PaiementServiceImpl(PaiementRepository repository, PaiementMapper mapper, NotificationService notificationService, StripeService stripeService, PayPalService payPalService, MoovMoneyService moovMoneyService, ArtelMoneyService artelMoneyService, SouscriptionRepository souscriptionRepository, SinistreRepository sinistreRepository, PrestationRepository prestationRepository, RecuPaiementRepository recuPaiementRepository) {
        super(Paiement.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.notificationService = notificationService;
        this.stripeService = stripeService;
        this.payPalService = payPalService;
        this.moovMoneyService = moovMoneyService;
        this.artelMoneyService = artelMoneyService;
        this.souscriptionRepository = souscriptionRepository;
        this.sinistreRepository = sinistreRepository;
        this.prestationRepository = prestationRepository;
        this.recuPaiementRepository = recuPaiementRepository;
    }

    @Transactional
    @LogExecution
    @Override
    public PaiementResponse save(PaiementRequest dto) throws RessourceNotFoundException {
        try {
            Paiement paiement = mapper.toEntity(dto);
            // Construire le paiement
            paiement = buildPaiement(paiement);
            return getOne(paiement);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public Paiement saveDefault(Paiement paiement) {
        // Construire le paiement
        // Cela persiste également le reçu de paiement grâce à la cascade
        return buildPaiement(paiement);
    }

    @Transactional
    @LogExecution
    private Paiement buildPaiement(Paiement paiement) {
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
        List<Paiement> paiements = repository.findBySouscriptionAndTypeIn(souscription, List.of(PaymentType.REMBOURSEMENT, PaymentType.PRESTATION));
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

        // Générer les détails du reçu en fonction du type de paiement
        String details = buildDetailsFromPaiement(paiement);

        // Sauvegarder le paiement
        repository.save(paiement);

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
                souscriptionRepository.save(souscription);  // Sauvegarder la souscription mise à jour
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

        return paiement;
    }

    @Transactional
    @LogExecution
    private String buildDetailsFromPaiement(Paiement paiement) {
        // Récupérer les informations de la souscription et du paiement
        Souscription souscription = paiement.getSouscription();
        if (souscription == null) {
            throw new RessourceNotFoundException("Impossible de traiter le paiement");
        }

        // Créer une pagination pour une seule entrée par page
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "datePaiement"));

        // Récupérer la date du dernier paiement effectué pour cette souscription
        Page<Paiement> paiementPage;
        if (paiement.getType() == PaymentType.PRIME) {
            paiementPage = repository.findLastPrimePaiementBySouscriptionId(souscription.getId(), pageable);
        } else {
            paiementPage = repository.findLastOtherPaiementBySouscriptionId(souscription.getId(), pageable);
        }

        Optional<Paiement> latestPaiementOpt = paiementPage.getContent().stream().findFirst();

        // Déterminer la date de début pour le calcul du délai
        LocalDate dateDebut = latestPaiementOpt.map(Paiement::getDatePaiement)
                .orElse(souscription.getDateSouscription());

        // Déterminer la période correspondante du paiement
        LocalDate delai = calculeDelai(dateDebut,
                souscription.getFrequencePaiement());

        // Vérifier que la date de paiement est présente
        LocalDate currentDatePaiement = paiement.getDatePaiement();

        if (currentDatePaiement != null
                && !currentDatePaiement.isEqual(delai)
                && !delai.isEqual(souscription.getDateSouscription())) {

            paiement.setDatePaiement(delai);
            log.info("Date de paiement effectuée : " + paiement.getDatePaiement());
        }

        // Format de date souhaité
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = delai.format(formatter);

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
    private LocalDate calculeDelai(LocalDate dateSouscription, PaymentFrequency frequencePaiement) {
        // Initialize a variable to hold the calculated delay period
        Period delayPeriod = switch (frequencePaiement) {
            case MENSUEL -> Period.ofMonths(1);
            case TRIMESTRIEL -> Period.ofMonths(3);
            case SEMESTRIEL -> Period.ofMonths(6);
            case ANNUEL -> Period.ofYears(1);
            default -> Period.ofYears(1);
        };

        // Calculate the delay based on the payment frequency
        // Add the delay period to the subscription date to get the expiration date
        return dateSouscription.plus(delayPeriod);
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
    public PaiementResponse findByNumeroPaiement(String numeroPaiement) {
        Paiement paiement = repository.findByNumeroPaiement(numeroPaiement)
                .orElseThrow(() -> new RessourceNotFoundException("Paiement avec reference " + numeroPaiement + " introuvable"));
        return mapper.toDto(paiement);
    }

    @Transactional
    @LogExecution
    @Override
    public List<PaiementResponse> findAllByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Paiement> paiements = repository.findAllByDateRange(startDate, endDate);
        return mapper.toDto(paiements);
    }

    @Transactional
    @LogExecution
    @Override
    public List<PaiementResponse> findAllBySouscriptionId(Long souscriptionId) {
        List<Paiement> paiements = repository.findAllBySouscriptionId(souscriptionId);
        return mapper.toDto(paiements);
    }

    @Transactional
    @LogExecution
    @Override
    public List<PaiementResponse> findByReclamationId(Long reclamationId) {
        List<Paiement> paiements = repository.findAllByReclamationId(reclamationId);
        return mapper.toDto(paiements);
    }

    @Transactional
    @LogExecution
    @Override
    public PaiementResponse findByRecuPaiementId(Long recuPaiementId) {
        Paiement paiement = repository.findByRecuPaiementId(recuPaiementId)
                .orElseThrow(() -> new RessourceNotFoundException("Paiement avec l'ID reçu " + recuPaiementId + " introuvable"));
        return mapper.toDto(paiement);
    }

    @Transactional
    @LogExecution
    @Override
    public PaiementResponse makePaiement(PublicPaiementRequest dto) {
        log.info("Démarrage de la méthode makePaiement avec le DTO : {}", dto);

        // Recherche de la souscription par ID
        Souscription souscription = souscriptionRepository.findById(dto.getSouscription())
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Souscription avec l'ID " + dto.getSouscription() + " introuvable"));
        log.info("Souscription récupérée : {}", souscription);

        // On calcul le montant effectif du paiement
        BigDecimal montantPaiement = calculeMontantTotalPrime(souscription.getMontantCotisation(), souscription.getFrequencePaiement());
        log.info("Montant total à payer : " + montantPaiement);

        // Construction de l'objet Paiement
        Paiement paiement = new Paiement();
        paiement.setMode(dto.getMode());
        paiement.setDatePaiement(LocalDate.now());
        paiement.setMontant(montantPaiement);
        paiement.setSouscription(souscription);
        paiement.setType(PaymentType.PRIME);
        log.info("Paiement préparé avec les informations : {}", paiement);

        // Construire le paiement avec la méthode buildPaiement
        paiement = buildPaiement(paiement);
        log.info("Paiement après appel à buildPaiement : {}", paiement);

        // Récupération de la réponse finale
        PaiementResponse response = getOne(paiement);
        log.info("Réponse de paiement retournée : {}", response);

        return response;
    }

    @Transactional
    @LogExecution
    @Override
    public PaiementResponse getOne(Paiement entity) {
        PaiementResponse dto = mapper.toDto(entity);
        dto.setRecuPaiements(entity.getRecuPaiements().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return dto;
    }
}

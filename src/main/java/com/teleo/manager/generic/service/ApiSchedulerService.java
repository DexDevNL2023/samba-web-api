package com.teleo.manager.generic.service;

import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.assurance.enums.PaymentFrequency;
import com.teleo.manager.assurance.enums.SouscriptionStatus;
import com.teleo.manager.assurance.repositories.AssureRepository;
import com.teleo.manager.assurance.repositories.SouscriptionRepository;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.notification.enums.TypeNotification;
import com.teleo.manager.notification.services.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiSchedulerService {

    private final AssureRepository assureRepository;
    private final SouscriptionRepository souscriptionRepository;
    private final NotificationService notificationService;

    public ApiSchedulerService(AssureRepository assureRepository, SouscriptionRepository souscriptionRepository, NotificationService notificationService) {
        this.assureRepository = assureRepository;
        this.souscriptionRepository = souscriptionRepository;
        this.notificationService = notificationService;
    }

    // Exécuter toutes les 30 secondes
    @Transactional
    @LogExecution
    @Scheduled(fixedRate = 30000)
    public void checkForProfileAndReminderNotifications() {
        // Vérifier les modifications de profil
        checkProfileUpdates();

        // Vérifier les souscriptions proches de l'expiration
        checkSouscriptionsExpiringSoonReminders();

        // Vérifier les souscriptions dont le status est 'WAITING'
        checkSouscriptionsStatusIsWaitingReminders();

        // Appeler la méthode de traitement du paiement
        traiterPaiement();
    }

    private void traiterPaiement() {
        // Récupérer les souscriptions dont le statut est 'ON_RISK' et qui nécessitent un paiement
        LocalDate today = LocalDate.now();
        List<Souscription> souscriptions = souscriptionRepository.findSouscriptionsIsNotExpirate(today);

        for (Souscription souscription : souscriptions) {
            // Calculer la prochaine échéance
            LocalDate prochaineEcheance = calculeDelai(souscription.getDateSouscription(), souscription.getFrequencePaiement());

            // Vérifier si la prochaine échéance est supérieure ou égale à la date actuelle
            if (!souscription.getStatus().equals(SouscriptionStatus.RESILIE) &&
                    (today.isEqual(prochaineEcheance) ||
                            today.isAfter(prochaineEcheance))) {

                // Mettre à jour le statut à 'WAITING'
                souscription.setStatus(SouscriptionStatus.WAITING);
                souscriptionRepository.save(souscription);

                // Envoyer une notification
                notificationService.generateNotification(null,
                        souscription.getAssure().getAccount(),
                        "Couverture Suspendue",
                        "Votre couverture pour la police " + souscription.getPolice().getLabel() +
                                " a été suspendue en raison de non-paiement. Veuillez régulariser la situation.",
                        TypeNotification.CLAIM);
            }
        }
    }

    // Vérifier les mises à jour des profils d'assurés
    @Transactional
    @LogExecution
    private void checkProfileUpdates() {
        Instant thirtyMinutesAgo = Instant.now().minus(30, ChronoUnit.MINUTES);
        List<Assure> assuresWithUpdates = assureRepository.findAssuresWithUpdatedOrEmptyFields(thirtyMinutesAgo);

        for (Assure assure : assuresWithUpdates) {
            String nameFields = getNameEmptyFields(assure);
            notificationService.generateNotification(null,
                    assure.getAccount(),
                    "Mise à Jour de Profil",
                    "Les informations suivantes : " + nameFields + " de votre profil ne sont pas correctes.",
                    TypeNotification.PROFILE);
        }
    }

    // Vérifier les souscriptions proches de l'expiration et générer des rappels
    @Transactional
    @LogExecution
    private void checkSouscriptionsExpiringSoonReminders() {
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        List<Souscription> expiringSouscriptions = souscriptionRepository.findSouscriptionsExpiringSoon(thirtyDaysFromNow);

        for (Souscription souscription : expiringSouscriptions) {
            notificationService.generateNotification(null,
                    souscription.getAssure().getAccount(),
                    "Rappel de Renouvellement",
                    "Votre " + souscription.getPolice().getLabel() + " arrive à expiration le " + souscription.getDateExpiration() + ". " +
                            "Veuillez la renouveler pour continuer à bénéficier de nos services.",
                    TypeNotification.REMINDER);
        }
    }

    // Vérifier les souscriptions dont le statut est 'WAITING' et générer des rappels
    @Transactional
    @LogExecution
    private void checkSouscriptionsStatusIsWaitingReminders() {
        List<Souscription> waitingSouscriptions = souscriptionRepository.findSouscriptionsStatusIsWaiting(SouscriptionStatus.WAITING);

        for (Souscription souscription : waitingSouscriptions) {
            notificationService.generateNotification(null,
                    souscription.getAssure().getAccount(),
                    "Rappel de Paiement",
                    "Votre " + souscription.getPolice().getLabel() + " est en attente de paiement. " +
                            "Vous avez jusqu'au " + calculeDelai(souscription.getDateSouscription(), souscription.getFrequencePaiement()) + " pour effectuer le paiement de votre prime.",
                    TypeNotification.REMINDER);
        }
    }

    @Transactional
    @LogExecution
    private LocalDate calculeDelai(LocalDate dateSouscription, PaymentFrequency frequencePaiement) {
        switch (frequencePaiement) {
            case MENSUEL:
                return dateSouscription.plusMonths(1);
            case TRIMESTRIEL:
                return dateSouscription.plusMonths(3);
            case SEMESTRIEL:
                return dateSouscription.plusMonths(6);
            case ANNUEL:
                return dateSouscription.plusYears(1);
            default:
                throw new IllegalArgumentException("Fréquence de paiement non reconnue : " + frequencePaiement);
        }
    }

    @Transactional
    @LogExecution
    private String getNameEmptyFields(Assure assure) {
        List<String> emptyFields = new ArrayList<>();
        if (assure.getNumNiu() == null || assure.getNumNiu().isEmpty()) emptyFields.add("Numéro NIU");
        if (assure.getDateNaissance() == null) emptyFields.add("Date de Naissance");
        if (assure.getNumCni() == null || assure.getNumCni().isEmpty()) emptyFields.add("Numéro CNI");
        if (assure.getEmail() == null || assure.getEmail().isEmpty()) emptyFields.add("Email");
        if (assure.getTelephone() == null || assure.getTelephone().isEmpty()) emptyFields.add("Téléphone");
        if (assure.getSignature() == null || assure.getSignature().isEmpty()) emptyFields.add("Signature");

        return String.join(", ", emptyFields);
    }
}
